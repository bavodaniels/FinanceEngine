package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.accounting.Accounting;
import be.bavodaniels.finance.model.Transaction;
import be.bavodaniels.finance.model.TransactionType;
import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.risktargetcalculator.RiskTargetCalculator;
import be.bavodaniels.finance.ta.TA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractBuyAndHoldVariablePositionStrategy implements Strategy {
    private final PriceRepository priceRepository;
    private final String asset;
    private final List<Transaction> transactions = new ArrayList<>();
    private final double allocatedCapital;
    private final double startingCapital;
    private final RiskTargetCalculator riskTargetCalculator;
    private final Accounting accounting;
    private final TA ta;


    protected AbstractBuyAndHoldVariablePositionStrategy(PriceRepository priceRepository,
                                                         String asset,
                                                         double allocatedCapital,
                                                         RiskTargetCalculator riskTargetCalculator,
                                                         Accounting accounting,
                                                         TA ta) {
        this.priceRepository = priceRepository;
        this.asset = asset;
        this.allocatedCapital = allocatedCapital;
        this.riskTargetCalculator = riskTargetCalculator;
        this.accounting = accounting;
        this.startingCapital = allocatedCapital;
        this.ta = ta;
    }

    @Override
    public void run(LocalDate date) {
        int forecast = ta.confidence(date);
        int amountOfOpenContracts = getAmountOfOpenContracts();
        double underlyingPrice = priceRepository.getUnderlyingPrice(asset, date);
        double currentPrice = priceRepository.getPrice(asset, date);
        if (forecast > 0) { // Uptrend
            int contractsToHold = riskTargetCalculator.calculateContractsToHold(allocatedCapital, underlyingPrice, date);

            if (contractsToHold != amountOfOpenContracts) {
                int amountToTransact = contractsToHold - amountOfOpenContracts;
                TransactionType transactionType = amountToTransact > 0 ? TransactionType.LONG : TransactionType.SHORT;
                transactions.add(new Transaction(date, currentPrice, Math.abs(amountToTransact), transactionType));
            }
        } else if (amountOfOpenContracts > 0) { // Downtrend and holding contracts
            transactions.add(new Transaction(date, currentPrice, amountOfOpenContracts, TransactionType.SHORT));
        }
        accounting.register(date, currentPrice, underlyingPrice, (double) getAmountOfOpenContracts());
    }

    private int getAmountOfOpenContracts() {
        int contractsHeld = transactions.stream()
                .map(transaction -> transaction.type() == TransactionType.LONG ? transaction.amount() : transaction.amount() * -1)
                .reduce(Integer::sum)
                .orElse(0);
        return contractsHeld;
    }

    @Override
    public Statistics getStatistics() {
        return accounting.getStatistics(startingCapital);
    }
}

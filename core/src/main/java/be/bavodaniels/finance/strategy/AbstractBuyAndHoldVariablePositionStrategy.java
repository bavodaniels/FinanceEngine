package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.accounting.Accounting;
import be.bavodaniels.finance.model.Transaction;
import be.bavodaniels.finance.model.TransactionType;
import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.risktargetcalculator.RiskTargetCalculator;

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


    protected AbstractBuyAndHoldVariablePositionStrategy(PriceRepository priceRepository,
                                                         String asset,
                                                         double allocatedCapital,
                                                         RiskTargetCalculator riskTargetCalculator,
                                                         Accounting accounting) {
        this.priceRepository = priceRepository;
        this.asset = asset;
        this.allocatedCapital = allocatedCapital;
        this.riskTargetCalculator = riskTargetCalculator;
        this.accounting = accounting;
        this.startingCapital = allocatedCapital;
    }

    @Override
    public void run(LocalDate date) {
        Double price = priceRepository.getPrice(asset, date);
        Double underlyingPrice = priceRepository.getUnderlyingPrice(asset, date);


        int contractsHeld = riskTargetCalculator.calculateContractsToHold(allocatedCapital, underlyingPrice, date);
        transactions.add(new Transaction(date, price, contractsHeld, TransactionType.BUY));

        accounting.register(date, price, underlyingPrice, contractsHeld);
    }

    @Override
    public void sellAll(LocalDate date) {
        int amountOfOpenContracts = getAmountOfOpenContracts();
        if (amountOfOpenContracts > 0) {
            Double price = priceRepository.getPrice(asset, date);
            transactions.add(new Transaction(date, price, amountOfOpenContracts, TransactionType.SELL));
        }
    }

    private int getAmountOfOpenContracts() {
        int contractsHeld = transactions.stream()
                .map(transaction -> transaction.type() == TransactionType.BUY ? transaction.amount() : transaction.amount() * -1)
                .reduce(Integer::sum)
                .orElse(0);
        return contractsHeld;
    }

    @Override
    public Statistics getStatistics() {
        return accounting.getStatistics(startingCapital);
    }
}

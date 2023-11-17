package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.model.Transaction;
import be.bavodaniels.finance.model.TransactionType;
import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.standarddeviation.FullDataSetStandardDeviation;
import be.bavodaniels.finance.standarddeviation.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.apache.commons.math3.stat.ranking.NaNStrategy;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class BuyAndHoldVariablePositionImplFullDataStdDev implements Strategy {
    private final PriceRepository priceRepository;
    private static final int minimalContractsToHold = 4;
    private final String asset;
    private final int multiplier;
    private final List<Transaction> transactions = new ArrayList<>();
    private final double allocatedCapital;
    private final double targetRisk;
    private final StandardDeviation stddev;
    private final DateColumn dateColumn = DateColumn.create("date");
    private final DoubleColumn backAdjustedPriceColumn = DoubleColumn.create("backAdjustedPrice");
    private final DoubleColumn actualPriceColumn = DoubleColumn.create("actualPrice");
    private final IntColumn contractsHeldColumn = IntColumn.create("contractsHeld");
    private Table accounting = Table.create("accounting",
            dateColumn,
            backAdjustedPriceColumn,
            actualPriceColumn,
            contractsHeldColumn
    );

    public BuyAndHoldVariablePositionImplFullDataStdDev(PriceRepository priceRepository,
                                                        String asset,
                                                        int multiplier,
                                                        double allocatedCapital,
                                                        double targetRisk) {
        this.priceRepository = priceRepository;
        this.asset = asset;
        this.multiplier = multiplier;
        this.allocatedCapital = allocatedCapital;
        this.targetRisk = targetRisk;

        this.stddev = new FullDataSetStandardDeviation(priceRepository, asset);
    }

    protected BuyAndHoldVariablePositionImplFullDataStdDev(PriceRepository priceRepository,
                                                        String asset,
                                                        int multiplier,
                                                        double allocatedCapital,
                                                        double targetRisk,
                                                           StandardDeviation stddev) {
        this.priceRepository = priceRepository;
        this.asset = asset;
        this.multiplier = multiplier;
        this.allocatedCapital = allocatedCapital;
        this.targetRisk = targetRisk;

        this.stddev = stddev;
    }

    @Override
    public void run(LocalDate date) {
        dateColumn.append(date);
        Double price = priceRepository.getPrice(asset, date);
        Double underlyingPrice = priceRepository.getUnderlyingPrice(asset, date);

        if (price == null) {
            price = getPreviousWorkingDayPrice(date);
            underlyingPrice = getPreviousWorkingDayUnderlyingPrice(date);
        }

        backAdjustedPriceColumn.append(price);
        actualPriceColumn.append(underlyingPrice);

        int contractsToHold = 0;
        if (minimalCapitalRequirementIsMet(date)) {
            contractsToHold = calculateContractsToHold(underlyingPrice, date);
            transactions.add(new Transaction(date, price, contractsToHold, TransactionType.BUY));
        }
        contractsHeldColumn.append(contractsToHold);
    }

    private int calculateContractsToHold(Double underlyingPrice, LocalDate date) {
        double contractsFractional = (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stddev.calculate(date));
        int wholeContracts = Double.valueOf(contractsFractional).intValue();
        return  wholeContracts >= minimalContractsToHold ? wholeContracts : 0;
    }

    private int getAmountOfOpenContracts() {
        int contractsHeld = transactions.stream()
                .map(transaction -> transaction.type() == TransactionType.BUY ? transaction.amount() : transaction.amount() * -1)
                .reduce(Integer::sum)
                .orElse(0);
        return contractsHeld;
    }

    private boolean minimalCapitalRequirementIsMet(LocalDate date) {
        return allocatedCapital > ((multiplier * getPreviousWorkingDayPrice(date) * 1.0 * stddev.calculate(date)) / targetRisk);
    }

    private Double getPreviousWorkingDayPrice(LocalDate date) {
        int daysToSubstract = 1;
        Double price = priceRepository.getPrice(asset, date.minusDays(daysToSubstract));
        while (price == null) {
            daysToSubstract++;
            price = priceRepository.getPrice(asset, date.minusDays(daysToSubstract));
        }
        return price;
    }

    private Double getPreviousWorkingDayUnderlyingPrice(LocalDate date) {
        int daysToSubstract = 1;
        Double price = priceRepository.getUnderlyingPrice(asset, date.minusDays(daysToSubstract));
        while (price == null) {
            daysToSubstract++;
            price = priceRepository.getUnderlyingPrice(asset, date.minusDays(daysToSubstract));
        }
        return price;
    }

    @Override
    public void sellAll(LocalDate date) {
        int amountOfOpenContracts = getAmountOfOpenContracts();
        if (amountOfOpenContracts > 0) {
            Double price = priceRepository.getPrice(asset, date);
            transactions.add(new Transaction(date, price, amountOfOpenContracts, TransactionType.SELL));
        }
    }

    @Override
    public Statistics getStatistics() {
        accounting = accounting.dropWhere(accounting.dateColumn("date").eval((Predicate<LocalDate>) localDate -> localDate.getDayOfWeek().ordinal() > 4));
        DateColumn dateColumn = accounting.dateColumn("date");
        DoubleColumn backAdjustedPrice = accounting.doubleColumn("backAdjustedPrice");
        IntColumn contractsHeld = accounting.intColumn("contractsHeld");
        DoubleColumn actualPrice = accounting.doubleColumn("actualPrice");

        DoubleColumn returnPricePoints = DoubleColumn.create("returnPricePoints")
                .append(DoubleColumn.create("priceChange")
                        .append(backAdjustedPrice)
                        .subtract(backAdjustedPrice.lag(1))
                        .multiply(contractsHeld.lag(1)));
        DoubleColumn returnBaseCurrency = DoubleColumn.create("returnBaseCurrency")
                .append(returnPricePoints.multiply(multiplier));
        DoubleColumn returns = DoubleColumn.create("returnPercentage")
                .append(returnBaseCurrency.divide(allocatedCapital));
        DoubleColumn cumsum = DoubleColumn.create("CumulativeSum")
                .append((returns)
                        .cumSum());
        DoubleColumn maxCumsum = DoubleColumn.create("maxCumSum")
                .append(cumsum.get(0));

        double mean = returns.mean();

        for (int i = 1; i < cumsum.size(); i++) {
            maxCumsum.append(Math.max(maxCumsum.get(i - 1), cumsum.get(i)));
        }
        DoubleColumn drawdown = DoubleColumn.create("DrawDown").append(maxCumsum.subtract(cumsum));
        DoubleColumn deMeaned = DoubleColumn.create("deMeaned").append(returns.subtract(mean));

        accounting.addColumns(returnPricePoints, returnBaseCurrency, returns, cumsum, maxCumsum, drawdown, deMeaned);
        Percentile p = new Percentile().withEstimationType(Percentile.EstimationType.R_6)
                .withNaNStrategy(NaNStrategy.REMOVED);
        p.setData(deMeaned.asDoubleArray());

        accounting.where(dateColumn.eval((Predicate<LocalDate>) localDate -> localDate.getDayOfWeek().ordinal() < 5))
                .write().csv(this.getClass().getSimpleName() + "-" + asset + ".csv");
        return new Statistics(returns.removeMissing().mean(),
                returns.removeMissing().standardDeviation(),
                drawdown.removeMissing().mean(),
                drawdown.removeMissing().max(),
                returns.removeMissing().skewness(),
                BigDecimal.valueOf(p.evaluate(1)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(p.evaluate(30)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(p.evaluate(70)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(p.evaluate(99)).setScale(8, RoundingMode.HALF_EVEN).doubleValue()
        );

        //0.01	-0.03203738
        //0.3	-0.003141953
        //0.7	0.00398731
        //0.99	0.031177895
    }
}

package be.bavodaniels.finance.accounting;

import be.bavodaniels.finance.accounting.percentile.PercentileCalculator;
import be.bavodaniels.finance.collection.StatisticalList;
import be.bavodaniels.finance.strategy.Statistics;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the Accounting interface that tracks financial data and calculates statistics.
 */
public class AccountingImpl implements Accounting {
    public static final String BACK_ADJUSTED_PRICE = "backAdjustedPrice";
    public static final String CONTRACTS_HELD = "contractsHeld";
    private static final int CONTRACT_MULTIPLIER = 5;
    private final Map<String, Map<LocalDate, Double>> workbook;
    private final PercentileCalculator percentileCalculator;

    /**
     * Creates a new AccountingImpl instance with default settings.
     */
    public AccountingImpl() {
        this(new PercentileCalculator());
    }

    /**
     * Creates a new AccountingImpl instance with the specified percentile calculator.
     * 
     * @param percentileCalculator the percentile calculator to use
     */
    public AccountingImpl(PercentileCalculator percentileCalculator) {
        this.workbook = new LinkedHashMap<>();
        this.workbook.put(BACK_ADJUSTED_PRICE, new LinkedHashMap<>());
        this.workbook.put(CONTRACTS_HELD, new LinkedHashMap<>());
        this.percentileCalculator = percentileCalculator;
    }

    @Override
    public void register(LocalDate date, Double price, Double underlyingPrice, Double contractsHeld) {
        Objects.requireNonNull(date, "Date cannot be null");
        Objects.requireNonNull(contractsHeld, "Contracts held cannot be null");

        workbook.get(BACK_ADJUSTED_PRICE).put(date, price);
        workbook.get(CONTRACTS_HELD).put(date, contractsHeld);
    }

    @Override
    public Statistics getStatistics(double allocatedCapital) {
        if (allocatedCapital <= 0) {
            throw new IllegalArgumentException("Allocated capital must be positive");
        }

        StatisticalList returnPercentage = calculateReturnPercentage(allocatedCapital);
        StatisticalList shiftedReturnPercentage = returnPercentage.shift();
        StatisticalList drawdown = calculateDrawDown(returnPercentage);
        double maxDrawdown = drawdown.stream().max(Double::compareTo).orElse(Double.NaN);
        Double mean = shiftedReturnPercentage.mean();
        StatisticalList deMeaned = returnPercentage.subtract(mean);
        percentileCalculator.setData(deMeaned);

        return new Statistics(mean,
                shiftedReturnPercentage.standardDeviation(),
                drawdown.mean(),
                maxDrawdown,
                shiftedReturnPercentage.skewness(),
                percentileCalculator.calculate(1),
                percentileCalculator.calculate(30),
                percentileCalculator.calculate(70),
                percentileCalculator.calculate(99));
    }

    private static StatisticalList calculateDrawDown(StatisticalList returnPercentage) {
        StatisticalList cumSum = returnPercentage.cumSum();
        StatisticalList maxCumSum = cumSum.max();

        StatisticalList drawdown = maxCumSum.subtract(cumSum);
        return drawdown;
    }

    private StatisticalList calculateReturnPercentage(double allocatedCapital) {
        StatisticalList backAdjustedPrices = new StatisticalList(workbook.get(BACK_ADJUSTED_PRICE).values()).ffil();
        StatisticalList returnPricePoints = backAdjustedPrices.difference().multiply(workbook.get(CONTRACTS_HELD).values().stream().toList());
        StatisticalList returnBaseCurrency = returnPricePoints.multiply(CONTRACT_MULTIPLIER);

        StatisticalList returnPercentage = returnBaseCurrency.divide(allocatedCapital);
        return returnPercentage;
    }
}

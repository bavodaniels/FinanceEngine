package be.bavodaniels.finance.accounting;

import be.bavodaniels.finance.accounting.percentile.PercentileCalculator;
import be.bavodaniels.finance.collection.StatisticalList;
import be.bavodaniels.finance.strategy.Statistics;

import java.time.LocalDate;
import java.util.*;

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
                percentileCalculator.calculate(99),
                calculateTurnOver());
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

    public double calculateTurnOver() {
        // Get the contracts held values as a list, preserving the order from the map
        List<Double> contractsHeldValues = new ArrayList<>(workbook.get(CONTRACTS_HELD).values());

        List<Double> differences = new ArrayList<>();

        // Calculate differences between consecutive elements
        for (int i = 1; i < contractsHeldValues.size(); i++) {
            Double currentValue = contractsHeldValues.get(i);
            Double previousValue = contractsHeldValues.get(i - 1);

            // Handle potential NaN values by skipping the difference calculation
            if (!Double.isNaN(currentValue) && !Double.isNaN(previousValue)) {
                Double difference = currentValue - previousValue;
                differences.add(difference);
            }
        }

        List<Double> percentageOfTrade = new ArrayList<>();
        for (int i = 1; i < contractsHeldValues.size(); i++) {
            double contractsHeld = contractsHeldValues.get(i);
            double difference = differences.get(i - 1);

            if (contractsHeld == 0.0){
                percentageOfTrade.add(0.0);
            }else if (!Double.isNaN(contractsHeld) && !Double.isNaN(difference)) {
                percentageOfTrade.add(Math.abs(difference) / contractsHeld);
            }
        }

        double dailyTurnOver = percentageOfTrade.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);


        return dailyTurnOver * 256;
    }
}

package be.bavodaniels.finance.accounting;

import be.bavodaniels.finance.accounting.calculator.DrawdownCalculator;
import be.bavodaniels.finance.accounting.calculator.ReturnPercentageCalculator;
import be.bavodaniels.finance.accounting.calculator.TurnoverCalculator;
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
    private final Map<String, Map<LocalDate, Double>> workbook;
    private final PercentileCalculator percentileCalculator;
    private final ReturnPercentageCalculator returnPercentageCalculator;
    private final DrawdownCalculator drawdownCalculator;
    private final TurnoverCalculator turnoverCalculator;

    /**
     * Creates a new AccountingImpl instance with default settings.
     */
    public AccountingImpl() {
        this(new PercentileCalculator(), 
             new ReturnPercentageCalculator(), 
             new DrawdownCalculator(), 
             new TurnoverCalculator());
    }

    /**
     * Creates a new AccountingImpl instance with the specified percentile calculator.
     * 
     * @param percentileCalculator the percentile calculator to use
     */
    public AccountingImpl(PercentileCalculator percentileCalculator) {
        this(percentileCalculator, 
             new ReturnPercentageCalculator(), 
             new DrawdownCalculator(), 
             new TurnoverCalculator());
    }

    /**
     * Creates a new AccountingImpl instance with the specified calculators.
     * 
     * @param percentileCalculator the percentile calculator to use
     * @param returnPercentageCalculator the return percentage calculator to use
     * @param drawdownCalculator the drawdown calculator to use
     * @param turnoverCalculator the turnover calculator to use
     */
    public AccountingImpl(PercentileCalculator percentileCalculator,
                          ReturnPercentageCalculator returnPercentageCalculator,
                          DrawdownCalculator drawdownCalculator,
                          TurnoverCalculator turnoverCalculator) {
        this.workbook = new LinkedHashMap<>();
        this.workbook.put(BACK_ADJUSTED_PRICE, new LinkedHashMap<>());
        this.workbook.put(CONTRACTS_HELD, new LinkedHashMap<>());
        this.percentileCalculator = percentileCalculator;
        this.returnPercentageCalculator = returnPercentageCalculator;
        this.drawdownCalculator = drawdownCalculator;
        this.turnoverCalculator = turnoverCalculator;
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

        StatisticalList returnPercentage = returnPercentageCalculator.calculate(
                allocatedCapital, 
                workbook.get(BACK_ADJUSTED_PRICE), 
                workbook.get(CONTRACTS_HELD)
        );
        StatisticalList shiftedReturnPercentage = returnPercentage.shift();
        StatisticalList drawdown = drawdownCalculator.calculate(returnPercentage);
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
                turnoverCalculator.calculate(workbook.get(CONTRACTS_HELD)));
    }
}

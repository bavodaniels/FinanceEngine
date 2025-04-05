package be.bavodaniels.finance.accounting;

import be.bavodaniels.finance.collection.StatisticalList;
import be.bavodaniels.finance.strategy.Statistics;
import org.apache.commons.math4.legacy.stat.descriptive.rank.Percentile;
import org.apache.commons.math4.legacy.stat.ranking.NaNStrategy;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class AccountingImpl implements Accounting {
    public static final String BACK_ADJUSTED_PRICE = "backAdjustedPrice";
    public static final String UNDERLYING_PRICE = "underlyingPrice";
    public static final String CONTRACTS_HELD = "contractsHeld";
    private Map<String, Map<LocalDate, Double>> workbook = new LinkedHashMap<>();

    public AccountingImpl() {
        workbook.put(BACK_ADJUSTED_PRICE, new LinkedHashMap<>());
        workbook.put(UNDERLYING_PRICE, new LinkedHashMap<>());
        workbook.put(CONTRACTS_HELD, new LinkedHashMap<>());
    }

    @Override
    public void register(LocalDate date, Double price, Double underlyingPrice, Double contractsHeld) {
        workbook.get(BACK_ADJUSTED_PRICE).put(date, price);
        workbook.get(UNDERLYING_PRICE).put(date, price);
        workbook.get(CONTRACTS_HELD).put(date, contractsHeld);
    }

    @Override
    public Statistics getStatistics(double allocatedCapital) {
        StatisticalList backAdjustedPrices = new StatisticalList(workbook.get(BACK_ADJUSTED_PRICE).values()).ffil();
        StatisticalList returnPricePoints = backAdjustedPrices.difference().multiply(workbook.get(CONTRACTS_HELD).values().stream().toList());
        StatisticalList returnBaseCurrency = returnPricePoints.multiply(5);

        //i dont feel its correct to keep seeing the return percentage from the allocatedCapital
        StatisticalList returnPercentage = returnBaseCurrency.divide(allocatedCapital);


        StatisticalList shiftedReturnPercentage = returnPercentage.shift();
        Double mean = shiftedReturnPercentage.mean();

        StatisticalList cumSum = returnPercentage.cumSum();
        StatisticalList maxCumSum = cumSum.max();

        StatisticalList drawdown = maxCumSum.subtract(cumSum);
        StatisticalList deMeaned = returnPercentage.subtract(mean);

        Percentile p = new Percentile().withEstimationType(Percentile.EstimationType.R_7)
                .withNaNStrategy(NaNStrategy.REMOVED);
        p.setData(deMeaned.parallelStream().mapToDouble(value -> value).toArray());
        return new Statistics(mean,
                shiftedReturnPercentage.standardDeviation(),
                drawdown.mean(),
                drawdown.stream().max(Double::compareTo).orElse(Double.NaN),
                shiftedReturnPercentage.skewness(),
                p.evaluate(1),
                p.evaluate(30),
                p.evaluate(70),
                p.evaluate(99));
    }
}

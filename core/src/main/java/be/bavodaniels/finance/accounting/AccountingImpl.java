package be.bavodaniels.finance.accounting;

import be.bavodaniels.finance.collection.StatisticalList;
import be.bavodaniels.finance.model.Transaction;
import be.bavodaniels.finance.strategy.Statistics;
import org.apache.commons.math4.legacy.stat.descriptive.rank.Percentile;
import org.apache.commons.math4.legacy.stat.ranking.NaNStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

        Percentile p = new Percentile().withEstimationType(Percentile.EstimationType.R_6)
                .withNaNStrategy(NaNStrategy.REMOVED);
        p.setData(deMeaned.parallelStream().mapToDouble(value -> value).toArray());
        return new Statistics(mean,
                shiftedReturnPercentage.standardDeviation(),
                drawdown.mean(),
                drawdown.stream().max(Double::compareTo).orElse(Double.NaN),
                shiftedReturnPercentage.skewness(),
                BigDecimal.valueOf(p.evaluate(1)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(p.evaluate(30)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(p.evaluate(70)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(p.evaluate(99)).setScale(8, RoundingMode.HALF_EVEN).doubleValue());
//                return new Statistics(mean,
//                returnPercentage.standardDeviation(),
//                drawdown.mean(),
//                drawdown.stream().max(Double::compareTo).orElse(0.0),
//                returnPercentage.skewness(),
//                BigDecimal.valueOf(p.evaluate(1)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
//                BigDecimal.valueOf(p.evaluate(30)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
//                BigDecimal.valueOf(p.evaluate(70)).setScale(8, RoundingMode.HALF_EVEN).doubleValue(),
//                BigDecimal.valueOf(p.evaluate(99)).setScale(8, RoundingMode.HALF_EVEN).doubleValue()
    }

    private double calculateTurnOver(List<Transaction> transactions) {
        List<Double> pctChange = new ArrayList<>();

        for (int i = 1; i < transactions.size(); i++) {
            Transaction currentTransaction = transactions.get(i);
            Transaction previousTransaction = transactions.get(i - 1);
            pctChange.add(calculatePercentageChange(currentTransaction, previousTransaction));
        }

        return pctChange.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    }

    private static double calculatePercentageChange(Transaction currentTransaction, Transaction previousTransaction) {
        if (previousTransaction.amount() != 0)
            return ((double) currentTransaction.amount() / (double) previousTransaction.amount()) - 1.0;
        else
            return 0.0;
    }
}

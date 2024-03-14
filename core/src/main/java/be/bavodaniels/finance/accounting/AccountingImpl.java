package be.bavodaniels.finance.accounting;

import be.bavodaniels.finance.model.Transaction;
import be.bavodaniels.finance.strategy.Statistics;
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

public class AccountingImpl implements Accounting {
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

    @Override
    public void register(LocalDate date, Double price, Double underlyingPrice, int contractsHeld) {
        dateColumn.append(date);
        backAdjustedPriceColumn.append(price);
        actualPriceColumn.append(underlyingPrice);
        contractsHeldColumn.append(contractsHeld);
    }

    @Override
    public Statistics getStatistics(double allocatedCapital) {
        accounting = accounting.dropWhere(accounting.dateColumn("date").eval((Predicate<LocalDate>) localDate -> localDate.getDayOfWeek().ordinal() > 4));
        DoubleColumn backAdjustedPrice = accounting.doubleColumn("backAdjustedPrice");
        IntColumn contractsHeld = accounting.intColumn("contractsHeld");

        DoubleColumn returnPricePoints = DoubleColumn.create("returnPricePoints")
                .append(DoubleColumn.create("priceChange")
                        .append(backAdjustedPrice)
                        .subtract(backAdjustedPrice.lag(1))
                        .multiply(contractsHeld.lag(1)));
        DoubleColumn returnBaseCurrency = DoubleColumn.create("returnBaseCurrency")
                .append(returnPricePoints.multiply(5));
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

//        accounting.where(dateColumn.eval((Predicate<LocalDate>) localDate -> localDate.getDayOfWeek().ordinal() < 5))
//                .write().csv(this.getClass().getSimpleName() + "-" + asset + ".csv");
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

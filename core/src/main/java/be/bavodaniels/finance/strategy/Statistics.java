package be.bavodaniels.finance.strategy;

public record Statistics(double averageDailyReturn,
                         double annualisedReturn,
                         double dailyStandardDeviation,
                         double annualisedStandardDeviation,
                         double dailySharpeRatio,
                         double annualisedSharpeRatio,
                         double averageDrawdown,
                         double maxDrawdown,
                         double skew,
                         double lowerTail,
                         double upperTail,
                         double turnover) {
    public Statistics(double averageDailyReturn,
                      double dailyStandardDeviation,
                      double averageDrawdown,
                      double maxDrawdown,
                      double skew,
                      double percentile1,
                      double percentile30,
                      double percentile70,
                      double percentile99,
                      double turnover
    ) {
        this(averageDailyReturn * 100,
                averageDailyReturn * 100 * 256,
                dailyStandardDeviation * 100,
                dailyStandardDeviation * 100 * 16,
                averageDailyReturn / dailyStandardDeviation,
                ((averageDailyReturn * 256) / (dailyStandardDeviation * 16)),
                averageDrawdown * 100,
                maxDrawdown * 100,
                skew,
                (percentile1 / percentile30) / 4.43,
                (percentile99 / percentile70) / 4.43,
                turnover);
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "averageDailyReturn=" + String.format("%.4f%%", averageDailyReturn) +
                ", annualisedReturn=" + String.format("%.2f%%", annualisedReturn) +
                ", dailyStandardDeviation=" + String.format("%.4f%%", dailyStandardDeviation) +
                ", annualisedStandardDeviation=" + String.format("%.2f%%", annualisedStandardDeviation) +
                ", dailySharpeRatio=" + String.format("%.2f", dailySharpeRatio) +
                ", annualisedSharpeRatio=" + String.format("%.2f", annualisedSharpeRatio) +
                ", averageDrawdown=" + String.format("%.2f%%", averageDrawdown) +
                ", maxDrawdown=" + String.format("%.2f%%", maxDrawdown) +
                ", skew=" + String.format("%.2f", skew) +
                ", lowerTail=" + String.format("%.2f", lowerTail) +
                ", upperTail=" + String.format("%.2f", upperTail) +
                ", turnover=" + String.format("%.2f", turnover) +
                '}';
    }
}

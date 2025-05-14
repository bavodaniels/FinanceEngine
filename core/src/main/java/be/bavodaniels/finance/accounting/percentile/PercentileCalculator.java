package be.bavodaniels.finance.accounting.percentile;

import org.apache.commons.math4.legacy.stat.descriptive.rank.Percentile;
import org.apache.commons.math4.legacy.stat.ranking.NaNStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * Calculates percentiles for a given dataset.
 * This class encapsulates the percentile calculation logic that was previously in AccountingImpl.
 */
public class PercentileCalculator {
    Percentile percentile;

    /**
     * Creates a new PercentileCalculator with default settings.
     * Uses R_7 estimation type and removes NaN values.
     */
    public PercentileCalculator() {
        this(Percentile.EstimationType.R_7,
                NaNStrategy.REMOVED);
    }
    
    /**
     * Creates a new PercentileCalculator with the specified settings.
     * 
     * @param estimationType the estimation type to use
     * @param nanStrategy the strategy for handling NaN values
     */
    public PercentileCalculator(Percentile.EstimationType estimationType,
                                NaNStrategy nanStrategy) {
        percentile = new Percentile()
                .withEstimationType(estimationType)
                .withNaNStrategy(nanStrategy);
    }

    public void setData(List<Double> data){
        percentile.setData(data.stream().mapToDouble(value -> value).toArray());
    }
    
    /**
     * Calculates the specified percentile for the given data.
     *
     * @param percentile the percentile to calculate (0-100)
     * @return the calculated percentile value
     */
    public double calculate(double percentile) {
        return this.percentile.evaluate(percentile);
    }

    public double[] calculate(int... percentiles) {
        return Arrays.stream(percentiles)
                .mapToDouble(p -> this.percentile.evaluate(p))
                .toArray();
    }
}
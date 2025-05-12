package be.bavodaniels.finance.accounting.percentile;

import org.apache.commons.math4.legacy.stat.descriptive.rank.Percentile;
import org.apache.commons.math4.legacy.stat.ranking.NaNStrategy;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PercentileCalculatorTest {

    private final List<Double> testData = Arrays.asList(
            -2.73, -2.64, -2.62, -2.59, -2.28, -2.22, -2.10, -2.05, -1.91, -1.81,
            -1.80, -1.80, -1.75, -1.64, -1.54, -1.53, -1.53, -1.51, -1.47, -1.41,
            -1.39, -1.31, -1.30, -1.25, -1.25, -1.25, -1.20, -1.19, -1.19, -1.15,
            -1.14, -1.09, -1.09, -0.99, -0.98, -0.96, -0.96, -0.91, -0.86, -0.84,
            -0.82, -0.81, -0.80, -0.74, -0.72, -0.70, -0.70, -0.69, -0.68, -0.65,
            -0.65, -0.63, -0.61, -0.60, -0.59, -0.58, -0.57, -0.56, -0.55, -0.54,
            -0.51, -0.48, -0.45, -0.45, -0.45, -0.43, -0.35, -0.32, -0.32, -0.30,
            -0.29, -0.27, -0.22, -0.20, -0.19, -0.17, -0.16, -0.15, -0.14, -0.12,
            -0.10, -0.09, -0.07, -0.07, -0.04, -0.04, -0.04, -0.04, -0.04, -0.04,
            -0.04, -0.01, -0.01, -0.01, -0.01, -0.01, 0.03, 0.04, 0.04, 0.05,
            0.05, 0.05, 0.06, 0.07, 0.08, 0.10, 0.11, 0.13, 0.14, 0.14,
            0.15, 0.16, 0.16, 0.17, 0.17, 0.19, 0.20, 0.20, 0.26, 0.27,
            0.29, 0.29, 0.30, 0.34, 0.35, 0.36, 0.38, 0.38, 0.39, 0.40,
            0.40, 0.40, 0.43, 0.45, 0.46, 0.49, 0.52, 0.54, 0.56, 0.57,
            0.58, 0.58, 0.60, 0.61, 0.62, 0.62, 0.64, 0.67, 0.72, 0.76,
            0.82, 0.83, 0.85, 0.88, 0.94, 0.95, 0.98, 0.98, 1.03, 1.04,
            1.06, 1.14, 1.15, 1.18, 1.27, 1.29, 1.30, 1.34, 1.36, 1.38,
            1.38, 1.46, 1.47, 1.49, 1.61, 1.72, 1.78, 1.78, 1.82, 1.83,
            1.85, 1.93, 1.97, 2.05, 2.06, 2.07, 2.10, 2.11, 2.11, 2.11,
            2.15, 2.16, 2.20, 2.29, 2.47, 2.53, 2.66, 2.71, 2.88, 4.19
    );

    @Test
    void testCalculateSinglePercentile() {
        PercentileCalculator calculator = new PercentileCalculator();
        calculator.setData(testData);
        double p1 = calculator.calculate(1);
        double p30 = calculator.calculate(30);
        double p70 = calculator.calculate(70);
        double p99 = calculator.calculate(99);

        assertEquals(-2.6202, p1, 0.0001);
        assertEquals(-0.5190, p30, 0.0001);
        assertEquals(0.5730, p70, 0.0001);
        assertEquals(2.7117, p99, 0.0001);
    }

    @Test
    void testCalculateMultiplePercentiles() {
        PercentileCalculator calculator = new PercentileCalculator();
        calculator.setData(testData);
        double[] percentiles = calculator.calculate(1, 30, 70, 99);

        assertArrayEquals(new double[]{-2.6202, -0.5190, 0.5730, 2.7117}, percentiles, 0.0001);
    }

    @Test
    void testCustomConfiguration() {
        PercentileCalculator calculator = new PercentileCalculator(
                Percentile.EstimationType.LEGACY, NaNStrategy.FIXED);
        calculator.setData(testData);
        double p50 = calculator.calculate(50);

        // The value will be different with LEGACY estimation
        assertEquals(0.05, p50, 0.0001);
    }
}
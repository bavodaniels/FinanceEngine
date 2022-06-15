package be.bavodaniels.core.ta;

import be.bavodaniels.core.statistics.StandardDeviation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StandardDeviationTest {
    private final StandardDeviation standardDeviation = new StandardDeviation();

    @Test
    void testDeviation() {
        List<Double> case1 = List.of(600.0, 470.0, 170.0, 430.0, 300.0);
        List<Double> case2 = List.of(1.0, 43.0, 2.0, 1.0, 2.0, 4.0, 12.0, 4.0, 12.0, 10.0);

        assertThat(standardDeviation.calculate(case1).get()).isEqualTo(164.7118696390761);
        assertThat(standardDeviation.calculate(case2).get()).isEqualTo(12.696893758361181);

    }
}
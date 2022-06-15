package be.bavodaniels.core.ta;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MovingAverageTest {
    private final MovingAverage movingAverage = new MovingAverage();

    @Test
    void testExactNumberOfTickersNeeded() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);

        assertEquals(Optional.of(3.0), movingAverage.calculate(data));
    }

    @Test
    void testMoreThanEnoughNumberOfTickersProvided() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        assertEquals(Optional.of(4.0), movingAverage.calculate(data));
    }
}
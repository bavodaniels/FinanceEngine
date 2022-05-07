package be.bavodaniels.core.ta;

import be.bavodaniels.core.ta.exception.NotEnoughDataException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovingAverageTest {
    private final MovingAverage movingAverage = new MovingAverage();

    @Test
    void testExactNumberOfTickersNeeded() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);

        assertEquals(Optional.of(3.0), movingAverage.calculate(data, 5));
    }

    @Test
    void testMoreThanEnoughNumberOfTickersProvided() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        assertEquals(Optional.of(3.0), movingAverage.calculate(data, 5));
    }

    @Test
    void testNotEnoughDataThrowsException() {
        assertThrows(NotEnoughDataException.class, () -> movingAverage.calculate(
                List.of(1.0), 5));
    }
}
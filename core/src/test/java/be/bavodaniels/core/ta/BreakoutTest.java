package be.bavodaniels.core.ta;

import be.bavodaniels.core.ta.exception.NotEnoughDataException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BreakoutTest {
    private final Breakout breakout = new Breakout();

    @Test
    void testExactNumberOfTickersNeeded() {
        List<Double> data = Arrays.asList(1.0, 6.0, 3.0, 4.0, 5.0);

        assertEquals(Optional.of(new Breakout.Result(1.0, 6.0)),
                breakout.calculate(data, 5));
    }

    @Test
    void testMoreThanEnoughNumberOfTickersProvided() {
        List<Double> data = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0);

        assertEquals(Optional.of(new Breakout.Result(1.0, 5.0)),
                breakout.calculate(data, 5));
    }

    @Test
    void testNotEnoughDataThrowsException() {
        assertThrows(NotEnoughDataException.class, () -> breakout.calculate(
                List.of(1.0), 5));
    }
}
package be.bavodaniels.finance.ta;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FixedValueTest {
    @Test
    void testConfidence() {
        assertEquals(10, new FixedValue(10).confidence(LocalDate.now()));
    }
}
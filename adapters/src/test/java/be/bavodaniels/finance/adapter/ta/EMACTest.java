package be.bavodaniels.finance.adapter.ta;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EMACTest {
    @Test
    void testLongIsAboveShort() {
        int confidence = new EMAC(date -> 20, date -> 30).confidence(LocalDate.now());

        assertEquals(-20, confidence);
    }

    @Test
    void testShortIsAboveLong() {
        int confidence = new EMAC(date -> 20, date -> 10).confidence(LocalDate.now());

        assertEquals(20, confidence);
    }
}
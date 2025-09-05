package be.bavodaniels.finance.adapter.ta.indicator;

import be.bavodaniels.finance.port.repository.PriceRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EMATest {
    private final Map<LocalDate, Double> testData = Map.of(LocalDate.now(), 19.0,
            LocalDate.now().minusDays(1), 14.0,
            LocalDate.now().minusDays(2), 15.0,
            LocalDate.now().minusDays(3), 12.0,
            LocalDate.now().minusDays(4), 20.0,
            LocalDate.now().minusDays(5), 27.0,
            LocalDate.now().minusDays(6), 16.0,
            LocalDate.now().minusDays(7), 14.0,
            LocalDate.now().minusDays(8), 20.0,
            LocalDate.now().minusDays(9), 25.0
            );
    private final EMA ema = new EMA(4, new PriceRepository() {
        @Override
        public Double getPrice(String asset, LocalDate date) {
            return testData.get(date);
        }
    },"biscuits");

    @Test
    void testCalculateEma() {
        assertEquals(16.7488, ema.calculate(LocalDate.now()));
    }

    @Test
    void testNotEnoughData() {
        assertEquals(0.0, ema.calculate(LocalDate.now().minusYears(1)));
    }
}
package be.bavodaniels.finance.ta;

import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.ta.indicator.EMA;

import java.time.LocalDate;

public class EMAC implements TA {
    private final EMA shortEma;
    private final EMA longEma;

    public EMAC(int shortLookback, int longLookback, PriceRepository priceRepository, String asset) {
        this.shortEma = new EMA(shortLookback, priceRepository, asset);
        this.longEma = new EMA(longLookback, priceRepository, asset);
    }

    @Override
    public int confidence(LocalDate date) {
        return shortEma.calculate(date) > longEma.calculate(date) ? 20 : -20;
    }
}

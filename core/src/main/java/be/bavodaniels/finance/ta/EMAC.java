package be.bavodaniels.finance.ta;

import be.bavodaniels.finance.ta.indicator.Indicator;

import java.time.LocalDate;

public class EMAC implements TA {
    private final Indicator shortEma;
    private final Indicator longEma;

    public EMAC(Indicator shortEma, Indicator longEma) {
        this.shortEma = shortEma;
        this.longEma = longEma;
    }

    @Override
    public int confidence(LocalDate date) {
        return shortEma.calculate(date) > longEma.calculate(date) ? 20 : -20;
    }
}

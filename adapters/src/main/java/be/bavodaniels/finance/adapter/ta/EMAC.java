package be.bavodaniels.finance.adapter.ta;

import be.bavodaniels.finance.adapter.ta.indicator.Indicator;
import be.bavodaniels.finance.port.ta.TA;

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

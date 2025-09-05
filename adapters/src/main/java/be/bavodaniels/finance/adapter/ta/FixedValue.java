package be.bavodaniels.finance.adapter.ta;

import be.bavodaniels.finance.port.ta.TA;

import java.time.LocalDate;

public class FixedValue implements TA {
    private final int confidence;

    public FixedValue(int confidence) {
        this.confidence = confidence;
    }

    @Override
    public int confidence(LocalDate date) {
        return confidence;
    }
}

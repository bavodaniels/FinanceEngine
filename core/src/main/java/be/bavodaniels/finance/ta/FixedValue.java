package be.bavodaniels.finance.ta;

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

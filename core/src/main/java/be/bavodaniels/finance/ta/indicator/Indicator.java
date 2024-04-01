package be.bavodaniels.finance.ta.indicator;

import java.time.LocalDate;

public interface Indicator {
    double calculate(LocalDate date);
}

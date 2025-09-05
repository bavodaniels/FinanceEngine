package be.bavodaniels.finance.adapter.ta.indicator;

import java.time.LocalDate;

public interface Indicator {
    double calculate(LocalDate date);
}

package be.bavodaniels.finance.port.standarddeviation;

import java.time.LocalDate;

public interface StandardDeviation {
    double calculate(LocalDate date);
}

package be.bavodaniels.finance.ta;

import java.time.LocalDate;

public interface TA {
    int confidence(LocalDate date);
}

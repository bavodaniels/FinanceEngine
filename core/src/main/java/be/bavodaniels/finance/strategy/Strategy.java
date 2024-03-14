package be.bavodaniels.finance.strategy;

import java.time.LocalDate;

public interface Strategy {
    void run(LocalDate date);
    Statistics getStatistics();
}

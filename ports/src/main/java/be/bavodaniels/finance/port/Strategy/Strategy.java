package be.bavodaniels.finance.port.Strategy;

import be.bavodaniels.finance.port.model.Statistics;

import java.time.LocalDate;

public interface Strategy {
    void run(LocalDate date);
    Statistics getStatistics();
}

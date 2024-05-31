package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.Portfolio;

import java.time.LocalDate;

public interface Strategy {
    void execute(Portfolio portfolio, LocalDate date);
}

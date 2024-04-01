package be.bavodaniels.finance.accounting;

import be.bavodaniels.finance.strategy.Statistics;

import java.time.LocalDate;

public interface Accounting {

    void register(LocalDate date, Double price, Double underlyingPrice, Double contractsHeld);

    Statistics getStatistics(double allocatedCapital);
}

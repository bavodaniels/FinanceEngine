package be.bavodaniels.finance.port.accounting;


import be.bavodaniels.finance.port.model.Statistics;

import java.time.LocalDate;

public interface Accounting {

    void register(LocalDate date, Double price, Double underlyingPrice, Double contractsHeld);

    Statistics getStatistics(double allocatedCapital);
}

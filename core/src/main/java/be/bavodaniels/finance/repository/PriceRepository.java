package be.bavodaniels.finance.repository;

import java.time.LocalDate;
import java.util.List;


public interface PriceRepository {
    Double getCurrentPrice(String symbol);
    Double getPRice(String symbol, LocalDate date);
    List<Double> getBackAdjustedPrices(String symbol, LocalDate startDate, LocalDate endDate);
    List<Double> getHistoricalPrices(String symbol, LocalDate startDate, LocalDate endDate);
}

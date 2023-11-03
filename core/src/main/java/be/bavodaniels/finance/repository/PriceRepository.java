package be.bavodaniels.finance.repository;

import java.time.LocalDate;
import java.util.List;


public interface PriceRepository {
    Double getPrice(String asset, LocalDate date);
    Double getUnderlyingPrice(String asset, LocalDate date);

    List<Double> getPricesUpUntilDate(String asset, LocalDate date);
    List<Double> getPricesFromDataUpUntilDate(String asset, LocalDate from, LocalDate to);
}

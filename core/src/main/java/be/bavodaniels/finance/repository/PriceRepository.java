package be.bavodaniels.finance.repository;

import java.time.LocalDate;


public interface PriceRepository {
    Double getPrice(String asset, LocalDate date);
    Double getUnderlyingPrice(String asset, LocalDate date);
}

package be.bavodaniels.finance.port.repository;

import java.time.LocalDate;
import java.util.List;


public interface PriceRepository {
    default Double getPrice(String asset, LocalDate date) {
        return null;
    }
    default Double getUnderlyingPrice(String asset, LocalDate date) {
        return null;
    };

    default List<Double> getPricesUpUntilDate(String asset, LocalDate date){
        return null;
    };
    default List<Double> getPricesFromDateUpUntilDate(String asset, LocalDate from, LocalDate to){
        return null;
    };
}

package be.bavodaniels.finance.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeSeriesEntityRepository extends JpaRepository<Stock, Integer> {
    @Query("select t from Stock t where t.symbol = ?1")
    List<Stock> findAllByAsset(String symbol);
    @Query("select t.backadjustedPrice from Stock t where t.symbol = ?1 and t.date = ?2")
    Double findBackadjustedPrice(String symbol, LocalDate date);
    @Query("select t.underlyingPrice from Stock t where t.symbol = ?1 and t.date = ?2")
    Double findUnderlyingPrice(String symbol, LocalDate date);
    @Query("select t.backadjustedPrice from Stock t where t.symbol = ?1 and t.date <= ?2")
    List<Double> findBackadjustedPricesUntilDate(String symbol, LocalDate date);

    @Query("select t.backadjustedPrice from Stock t where t.symbol = ?1 and t.date >= ?2 and t.date <= ?3")
    List<Double> findBackadjustedPricesBetweenDates(String symbol, LocalDate from, LocalDate to);
}
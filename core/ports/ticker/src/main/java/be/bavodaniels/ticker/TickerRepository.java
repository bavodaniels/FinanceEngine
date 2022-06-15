package be.bavodaniels.ticker;

import java.time.ZonedDateTime;
import java.util.List;

public interface TickerRepository {
    List<String> getAllTickers();
    Double getPrice(String symbol);
    Double getPriceAtDate(String symbol, ZonedDateTime time);
}

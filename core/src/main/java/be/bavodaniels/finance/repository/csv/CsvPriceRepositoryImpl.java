package be.bavodaniels.finance.repository.csv;

import be.bavodaniels.finance.repository.PriceRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//@Component
public class CsvPriceRepositoryImpl implements PriceRepository {
    Map<String, Map<LocalDate, Double>> cache = new HashMap<>();
    Map<String, Map<LocalDate, Double>> underlyingCache = new HashMap<>();

    private Map<LocalDate, Double> getPrices(String symbol) {
        if (!cache.containsKey(symbol)) {
            try (Reader reader = new FileReader(new ClassPathResource(symbol + ".csv").getFile())) {
                Map<LocalDate, Double> output = new LinkedHashMap<>();
                List<TimeSeriesEntry> records = new CsvToBeanBuilder<TimeSeriesEntry>(reader)
                        .withType(TimeSeriesEntry.class).build().parse();
                records.forEach(record -> output.put(LocalDate.parse(record.getDate()), record.getAdjusted()));
                cache.put(symbol, output);
                return output;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return cache.get(symbol);
        }
    }

    private Map<LocalDate, Double> getUnderlyingPrices(String symbol) {
        if (!underlyingCache.containsKey(symbol)) {
            try (Reader reader = new FileReader(new ClassPathResource(symbol + ".csv").getFile())) {
                Map<LocalDate, Double> output = new LinkedHashMap<>();
                List<TimeSeriesEntry> records = new CsvToBeanBuilder<TimeSeriesEntry>(reader)
                        .withType(TimeSeriesEntry.class).build().parse();
                records.forEach(record -> output.put(LocalDate.parse(record.getDate()), record.getUnderlying()));
                underlyingCache.put(symbol, output);
                return output;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return underlyingCache.get(symbol);
        }
    }

    @Override
    public Double getPrice(String symbol, LocalDate date) {
        Map<LocalDate, Double> prices = getPrices(symbol);
        return getValue(date, prices);
    }

    @Override
    public Double getUnderlyingPrice(String symbol, LocalDate date) {
        Map<LocalDate, Double> prices = getUnderlyingPrices(symbol);
        return getValue(date, prices);
    }

    private static Double getValue(LocalDate date, Map<LocalDate, Double> prices) {
        Double price = null;
        long subtract = 0L;

        while(price == null){
            price = prices.get(date.minusDays(subtract++));
        }

        return price;
    }

    @Override
    public List<Double> getPricesUpUntilDate(String asset, LocalDate date) {
        Map<LocalDate, Double> prices = getPrices(asset);
        return prices.keySet()
                .parallelStream()
                .filter(d -> d.isBefore(date) || d.isEqual(date))
                .map(prices::get)
                .toList();
    }

    @Override
    public List<Double> getPricesFromDataUpUntilDate(String asset, LocalDate from, LocalDate to) {
        Map<LocalDate, Double> prices = getPrices(asset);
        return prices.keySet()
                .parallelStream()
                .filter(d -> (d.isBefore(to) || d.isEqual(to)) && (d.isAfter(from) || d.isEqual(from)))
                .map(prices::get)
                .toList();
    }

    public List<TimeSeriesEntry> findAll(String asset) {
        try (Reader reader = new FileReader(new ClassPathResource(asset + ".csv").getFile())) {
            Map<LocalDate, Double> output = new LinkedHashMap<>();
            List<TimeSeriesEntry> records = new CsvToBeanBuilder<TimeSeriesEntry>(reader)
                    .withType(TimeSeriesEntry.class).build().parse();
            records.forEach(record -> output.put(LocalDate.parse(record.getDate()), record.getUnderlying()));
            underlyingCache.put(asset, output);
            return records;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

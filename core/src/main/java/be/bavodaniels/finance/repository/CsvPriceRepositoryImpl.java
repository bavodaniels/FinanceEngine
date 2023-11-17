package be.bavodaniels.finance.repository;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CsvPriceRepositoryImpl implements PriceRepository {
    Map<String, Map<LocalDate, Double>> cache = new HashMap<>();
    Map<String, Map<LocalDate, Double>> underlyingCache = new HashMap<>();

    private Map<LocalDate, Double> getPrices(String symbol) {
        if (!cache.containsKey(symbol)) {
            try (Reader reader = new FileReader(new ClassPathResource("sp500.csv").getFile())) {
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
            try (Reader reader = new FileReader(new ClassPathResource("sp500.csv").getFile())) {
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
        return getPrices(symbol).get(date);
    }

    @Override
    public Double getUnderlyingPrice(String symbol, LocalDate date) {
        return getUnderlyingPrices(symbol).get(date);
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

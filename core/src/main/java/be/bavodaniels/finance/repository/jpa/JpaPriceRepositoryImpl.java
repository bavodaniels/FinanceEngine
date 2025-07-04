package be.bavodaniels.finance.repository.jpa;

import be.bavodaniels.finance.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JpaPriceRepositoryImpl implements PriceRepository {
    private static class StockCache {
        final Map<LocalDate, Double> backadjustedPrices = new HashMap<>();
        final Map<LocalDate, Double> underlyingPrices = new HashMap<>();
    }

    // Use ConcurrentHashMap for thread safety
    private final Map<String, StockCache> assetCache = new ConcurrentHashMap<>();
    private final TimeSeriesEntityRepository repository;

    @Autowired
    public JpaPriceRepositoryImpl(TimeSeriesEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Double getPrice(String asset, LocalDate date) {
        StockCache cache = assetCache.get(asset);
        if (cache != null && cache.backadjustedPrices.containsKey(date)) {
            return cache.backadjustedPrices.get(date);
        }

        try {
            Double price = repository.findBackadjustedPrice(asset, date);
            if (price != null) {
                ensureCacheExists(asset);
                assetCache.get(asset).backadjustedPrices.put(date, price);
                return price;
            }
        } catch (Exception e) {
            buildCacheIfNeeded(asset);
            Double price = assetCache.get(asset).backadjustedPrices.get(date);
            if (price != null) {
                return price;
            }
        }

        throw new RuntimeException("no price for this date");
    }

    @Override
    public Double getUnderlyingPrice(String asset, LocalDate date) {
        StockCache cache = assetCache.get(asset);
        if (cache != null && cache.underlyingPrices.containsKey(date)) {
            return cache.underlyingPrices.get(date);
        }

        try {
            Double price = repository.findUnderlyingPrice(asset, date);
            if (price != null) {
                ensureCacheExists(asset);
                assetCache.get(asset).underlyingPrices.put(date, price);
                return price;
            }
        } catch (Exception e) {
            buildCacheIfNeeded(asset);
            Double price = assetCache.get(asset).underlyingPrices.get(date);
            if (price != null) {
                return price;
            }
        }

        throw new RuntimeException("no price for this date");
    }

    @Override
    public List<Double> getPricesUpUntilDate(String asset, LocalDate date) {
        // Try to get directly from database first
        try {
            List<Double> prices = repository.findBackadjustedPricesUntilDate(asset, date);
            if (prices != null && !prices.isEmpty()) {
                return prices;
            }
        } catch (Exception e) {
            // Fall back to cache if direct query fails
        }

        // Fall back to cache
        buildCacheIfNeeded(asset);
        Map<LocalDate, Double> prices = assetCache.get(asset).backadjustedPrices;
        return prices.keySet()
                .parallelStream()
                .filter(d -> d.isBefore(date) || d.isEqual(date))
                .map(prices::get)
                .toList();
    }

    @Override
    public List<Double> getPricesFromDateUpUntilDate(String asset, LocalDate from, LocalDate to) {
        // Try to get directly from database first
        try {
            List<Double> prices = repository.findBackadjustedPricesBetweenDates(asset, from, to);
            if (prices != null && !prices.isEmpty()) {
                return prices;
            }
        } catch (Exception e) {
            // Fall back to cache if direct query fails
        }

        // Fall back to cache
        buildCacheIfNeeded(asset);
        Map<LocalDate, Double> prices = assetCache.get(asset).backadjustedPrices;
        return prices.keySet()
                .parallelStream()
                .filter(d -> (d.isBefore(to) || d.isEqual(to)) && (d.isAfter(from) || d.isEqual(from)))
                .map(prices::get)
                .toList();
    }

    private void ensureCacheExists(String asset) {
        assetCache.computeIfAbsent(asset, k -> new StockCache());
    }

    private void buildCacheIfNeeded(String asset) {
        // Use computeIfAbsent to ensure thread safety and avoid duplicate loading
        assetCache.computeIfAbsent(asset, k -> {
            StockCache cache = new StockCache();
            List<Stock> allData = repository.findAllByAsset(asset);

            // Populate both caches at once to avoid loading the same data twice
            for (Stock stock : allData) {
                cache.backadjustedPrices.put(stock.getDate(), stock.getBackadjustedPrice());
                cache.underlyingPrices.put(stock.getDate(), stock.getUnderlyingPrice());
            }

            return cache;
        });
    }
}

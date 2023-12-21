package be.bavodaniels.finance.repository.jpa;

import be.bavodaniels.finance.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JpaPriceRepositoryImpl implements PriceRepository {
    Map<String, Map<LocalDate, Double>> cache = new HashMap<>();
    Map<String, Map<LocalDate, Double>> underlyingCache = new HashMap<>();
    private final TimeSeriesEntityRepository repository;

    @Autowired
    public JpaPriceRepositoryImpl(TimeSeriesEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Double getPrice(String asset, LocalDate date) {
        buildCacheIfNeeded(asset, date);

        return cache.get(asset).get(date);
    }

    @Override
    public Double getUnderlyingPrice(String asset, LocalDate date) {
        buildUnderlyingCacheIfNeeded(asset, date);
        return underlyingCache.get(asset).get(date);
    }

    @Override
    public List<Double> getPricesUpUntilDate(String asset, LocalDate date) {
        buildUnderlyingCacheIfNeeded(asset, date);
        Map<LocalDate, Double> prices = cache.get(asset);
        return prices.keySet()
                .parallelStream()
                .filter(d -> d.isBefore(date) || d.isEqual(date))
                .map(prices::get)
                .toList();
    }

    @Override
    public List<Double> getPricesFromDataUpUntilDate(String asset, LocalDate from, LocalDate to) {
        buildCacheIfNeeded(asset, to);
        Map<LocalDate, Double> prices = cache.get(asset);
        return prices.keySet()
                .parallelStream()
                .filter(d -> (d.isBefore(to) || d.isEqual(to)) && (d.isAfter(from) || d.isEqual(from)))
                .map(prices::get)
                .toList();
    }

    private void buildCacheIfNeeded(String asset, LocalDate date){
        try {
            if (!cache.containsKey(asset)) {
                List<Stock> allData = repository.findAllByAsset(asset);
                //map allData to a Map
                cache.put(asset, allData.parallelStream().collect(Collectors.toMap(stock -> stock.getId().getDate(), Stock::getBackadjustedPrice)));
            }

            if (!cache.get(asset).containsKey(date)) {
                Double backadjustedPrice = repository.findBackadjustedPrice(asset, date);
                cache.get(asset).put(date, backadjustedPrice);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void buildUnderlyingCacheIfNeeded(String asset, LocalDate date){
        if (!underlyingCache.containsKey(asset)){
            List<Stock> allData = repository.findAllByAsset(asset);
            //map allData to a Map
            underlyingCache.put(asset, allData.parallelStream().collect(Collectors.toMap(timeSeriesEntity -> timeSeriesEntity.getId().getDate(), Stock::getUnderlyingPrice)));
        }

        if (!underlyingCache.get(asset).containsKey(date)){
            Double backadjustedPrice = repository.findBackadjustedPrice(asset, date);
            underlyingCache.get(asset).put(date, backadjustedPrice);
        }
    }
}

package be.bavodaniels.finance.adapter.standarddeviation;


import be.bavodaniels.finance.adapter.collection.StatisticalList;
import be.bavodaniels.finance.port.repository.PriceRepository;
import be.bavodaniels.finance.port.standarddeviation.StandardDeviation;

import java.time.LocalDate;
import java.util.List;

public class NLookBackStandardDeviation implements StandardDeviation {
    private final int lookback;
    private final PriceRepository priceRepository;
    private final String asset;
    public NLookBackStandardDeviation(int lookback, PriceRepository priceRepository, String asset) {
        this.lookback = lookback;
        this.priceRepository = priceRepository;
        this.asset = asset;
    }

    @Override
    public double calculate(LocalDate date) {
        List<Double> priceData = priceRepository.getPricesFromDateUpUntilDate(asset, date.minusDays(lookback), date);

        // Handle null or empty data
        if (priceData == null || priceData.isEmpty() || priceData.size() < 2) {
            return Double.NaN;
        }

        StatisticalList prices = new StatisticalList(priceData);
        return prices.pctChange().standardDeviation() * 16;
    }
}

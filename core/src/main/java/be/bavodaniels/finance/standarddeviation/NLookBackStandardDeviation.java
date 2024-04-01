package be.bavodaniels.finance.standarddeviation;


import be.bavodaniels.finance.collection.StatisticalList;
import be.bavodaniels.finance.repository.PriceRepository;

import java.time.LocalDate;

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
        StatisticalList prices = new StatisticalList(priceRepository.getPricesFromDateUpUntilDate(asset, date.minusDays(lookback), date));
        return prices.pctChange().standardDeviation() * 16;
    }
}

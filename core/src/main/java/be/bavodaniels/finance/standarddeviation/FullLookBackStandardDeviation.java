package be.bavodaniels.finance.standarddeviation;

import be.bavodaniels.finance.collection.StatisticalList;
import be.bavodaniels.finance.repository.PriceRepository;

import java.time.LocalDate;

public class FullLookBackStandardDeviation implements StandardDeviation {
    private final PriceRepository priceRepository;
    private final String asset;
    public FullLookBackStandardDeviation(PriceRepository priceRepository, String asset) {
        this.priceRepository = priceRepository;
        this.asset = asset;
    }

    @Override
    public double calculate(LocalDate date) {
        StatisticalList prices = new StatisticalList(priceRepository.getPricesUpUntilDate(asset, date));
        return prices.pctChange().standardDeviation() * 16;
    }
}

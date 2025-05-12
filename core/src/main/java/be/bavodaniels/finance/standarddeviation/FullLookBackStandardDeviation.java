package be.bavodaniels.finance.standarddeviation;

import be.bavodaniels.finance.collection.StatisticalList;
import be.bavodaniels.finance.repository.PriceRepository;

import java.time.LocalDate;
import java.util.List;

public class FullLookBackStandardDeviation implements StandardDeviation {
    private final PriceRepository priceRepository;
    private final String asset;
    public FullLookBackStandardDeviation(PriceRepository priceRepository, String asset) {
        this.priceRepository = priceRepository;
        this.asset = asset;
    }

    @Override
    public double calculate(LocalDate date) {
        List<Double> priceData = priceRepository.getPricesUpUntilDate(asset, date);

        // Handle null or empty data
        if (priceData == null || priceData.isEmpty() || priceData.size() < 2) {
            return Double.NaN;
        }

        StatisticalList prices = new StatisticalList(priceData);
        return prices.pctChange().standardDeviation() * 16;
    }
}

package be.bavodaniels.finance.standarddeviation;

import be.bavodaniels.finance.repository.PriceRepository;
import tech.tablesaw.api.DoubleColumn;

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
        List<Double> prices = priceRepository.getPricesUpUntilDate(asset, date);
        DoubleColumn col = DoubleColumn.create("prices", prices);
        return col.pctChange().standardDeviation() * 16;
    }
}

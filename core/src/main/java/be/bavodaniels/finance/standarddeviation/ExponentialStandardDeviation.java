package be.bavodaniels.finance.standarddeviation;


import be.bavodaniels.finance.repository.PriceRepository;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.time.LocalDate;

public class ExponentialStandardDeviation implements StandardDeviation {
    private final int SHORT_LOOKBACK = 32;
    private final int LONG_LOOKBACK_YEARS = 2600;
    private final PriceRepository priceRepository;
    private final String asset;

    private final Table standardDeviations = Table.create("standardDev");
    private final DoubleColumn price = DoubleColumn.create("price");
    private final DateColumn date = DateColumn.create("date");
    private final DateColumn stddev = DateColumn.create("stddev");
    public ExponentialStandardDeviation(PriceRepository priceRepository, String asset) {
        this.priceRepository = priceRepository;
        this.asset = asset;
        standardDeviations.addColumns(date, price);
    }

    @Override
    public double calculate(LocalDate date) {
        LocalDate fromDate = date.minusDays(SHORT_LOOKBACK);
        while (!fromDate.isEqual(date)){
            Double price = priceRepository.getPrice(asset, fromDate);
            fromDate = fromDate.plusDays(1);
            Row row = standardDeviations.appendRow();
            row.setDate("date", fromDate);
            row.setDouble("price", price);
            row.setDouble("stddev");
        }

        standardDeviations.addColumns(price.pctChange().rolling())

        price.pctChange().st
        double shortStandardDev = price.pctChange().standardDeviation() * 16;
        double longStandardDev = standardDeviations
        return (0.3 * longStandardDev) + (0.7 * shortStandardDev);
    }
}

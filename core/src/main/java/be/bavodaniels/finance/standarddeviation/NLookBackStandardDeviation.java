//package be.bavodaniels.finance.standarddeviation;
//
//
//import be.bavodaniels.finance.repository.PriceRepository;
//import tech.tablesaw.api.DoubleColumn;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public class NLookBackStandardDeviation implements StandardDeviation {
//    private final int lookback;
//    private final PriceRepository priceRepository;
//    private final String asset;
//    public NLookBackStandardDeviation(int lookback, PriceRepository priceRepository, String asset) {
//        this.lookback = lookback;
//        this.priceRepository = priceRepository;
//        this.asset = asset;
//    }
//
//    @Override
//    public double calculate(LocalDate date) {
//        List<Double> prices = priceRepository.getPrices(asset, date, 20);
//        DoubleColumn col = DoubleColumn.create("prices", prices);
//        return col.pctChange().standardDeviation() * 16;
//    }
//}

package be.bavodaniels.finance.standarddeviation;


import be.bavodaniels.finance.repository.PriceRepository;

import java.time.LocalDate;

public class ExponentialStandardDeviation implements StandardDeviation {
    private final int SHORT_LOOKBACK = 32;
    private final int LONG_LOOKBACK = 2600;

    private final StandardDeviation shortStdDev;
    private final StandardDeviation longStdDev;

    public ExponentialStandardDeviation(PriceRepository priceRepository, String asset) {
        this.shortStdDev = new NLookBackStandardDeviation(SHORT_LOOKBACK, priceRepository, asset);
        this.longStdDev = new NLookBackStandardDeviation(LONG_LOOKBACK, priceRepository, asset);
    }

    @Override
    public double calculate(LocalDate date) {
        return (longStdDev.calculate(date) * 0.3) + (shortStdDev.calculate(date) * 0.7);
    }
}

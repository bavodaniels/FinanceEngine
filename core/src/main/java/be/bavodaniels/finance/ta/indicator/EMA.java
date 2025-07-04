package be.bavodaniels.finance.ta.indicator;

import be.bavodaniels.finance.repository.PriceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EMA implements Indicator {
    private final int lookback;
    private final PriceRepository priceRepository;
    private final String asset;

    public EMA(int lookback, PriceRepository priceRepository, String asset) {
        this.lookback = lookback;
        this.priceRepository = priceRepository;
        this.asset = asset;
    }

    public double calculate(LocalDate date) {
        List<Double> prices = new ArrayList<>();

        try {
            prices = priceRepository.getPricesFromDateUpUntilDate(
                    asset,
                    date.minusDays(lookback * 2), // Get enough historical data
                    date
            );
        } catch (Exception e) {

        }

        if (prices == null || prices.isEmpty()) {
            return 0.0;
        }

        if (prices.size() > 1) {
            prices = new ArrayList<>(prices);
            Collections.reverse(prices);
            prices = prices.subList(0, lookback);
        }

        double oldValue = prices.get(0);
        double alpha = 2.0 / (lookback + 1.0);
        for (Double value : prices) {
            oldValue = oldValue * (1.0 - alpha) + value * alpha;
        }
        return oldValue;
    }
}

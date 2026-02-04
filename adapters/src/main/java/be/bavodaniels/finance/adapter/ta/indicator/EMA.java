package be.bavodaniels.finance.adapter.ta.indicator;


import be.bavodaniels.finance.port.repository.PriceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
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
        int i = 0;
        while (prices.size() <= lookback && i < lookback * 2) {
            try {
                Double price = priceRepository.getPrice(asset, date.minusDays(i++));
                if (price != null)
                    prices.add(price);
            }catch(Exception e){}
        }

        if(prices.isEmpty()){
            return 0.0;
        }
        //calculate the exponentional moving average of a list of doubles
        prices = prices.reversed();

        double oldValue = prices.get(0);
        double alpha = 2.0 / (lookback + 1.0);
        for (Double value : prices) {
            oldValue = oldValue * (1 - alpha) + value * alpha;
        }
        return oldValue;
    }
}

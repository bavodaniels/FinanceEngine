package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.standarddeviation.NLookBackStandardDeviation;


public class BuyAndHoldVariablePositionImplNLookBackStdDev extends AbstractBuyAndHoldVariablePositionStrategy {
    public BuyAndHoldVariablePositionImplNLookBackStdDev(PriceRepository priceRepository,
                                                         String asset,
                                                         int multiplier,
                                                         double allocatedCapital,
                                                         double targetRisk,
                                                         int lookBack) {
        super(priceRepository, asset, multiplier, allocatedCapital, targetRisk, new NLookBackStandardDeviation(lookBack, priceRepository, asset));
    }
}

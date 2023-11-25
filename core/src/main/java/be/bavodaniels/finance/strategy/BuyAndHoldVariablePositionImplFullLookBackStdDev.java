package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.standarddeviation.FullLookBackStandardDeviation;


public class BuyAndHoldVariablePositionImplFullLookBackStdDev extends AbstractBuyAndHoldVariablePositionStrategy {
    public BuyAndHoldVariablePositionImplFullLookBackStdDev(PriceRepository priceRepository,
                                                            String asset,
                                                            int multiplier,
                                                            double allocatedCapital,
                                                            double targetRisk) {
        super(priceRepository, asset, multiplier, allocatedCapital, targetRisk, new FullLookBackStandardDeviation(priceRepository, asset));
    }
}

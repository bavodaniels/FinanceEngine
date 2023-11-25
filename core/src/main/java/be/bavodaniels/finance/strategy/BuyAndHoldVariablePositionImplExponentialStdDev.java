package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.standarddeviation.ExponentialStandardDeviation;


public class BuyAndHoldVariablePositionImplExponentialStdDev extends AbstractBuyAndHoldVariablePositionStrategy {
    public BuyAndHoldVariablePositionImplExponentialStdDev(PriceRepository priceRepository,
                                                           String asset,
                                                           int multiplier,
                                                           double allocatedCapital,
                                                           double targetRisk) {
        super(priceRepository, asset, multiplier, allocatedCapital, targetRisk, new ExponentialStandardDeviation(priceRepository, asset));
    }
}

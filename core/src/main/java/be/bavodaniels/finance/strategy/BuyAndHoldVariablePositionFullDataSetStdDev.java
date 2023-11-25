package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.standarddeviation.FullDataSetStandardDeviation;

public class BuyAndHoldVariablePositionFullDataSetStdDev extends AbstractBuyAndHoldVariablePositionStrategy {
    public BuyAndHoldVariablePositionFullDataSetStdDev(PriceRepository priceRepository,
                                                          String asset,
                                                          int multiplier,
                                                          double allocatedCapital,
                                                          double targetRisk) {
        super(priceRepository, asset, multiplier, allocatedCapital, targetRisk, new FullDataSetStandardDeviation(priceRepository, asset));
    }
}

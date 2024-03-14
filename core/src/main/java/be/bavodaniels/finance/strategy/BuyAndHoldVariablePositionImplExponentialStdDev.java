package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.repository.NonNullPriceRepositoryImpl;
import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.risktargetcalculator.RiskTargetCalculator;
import be.bavodaniels.finance.standarddeviation.ExponentialStandardDeviation;


public class BuyAndHoldVariablePositionImplExponentialStdDev extends AbstractBuyAndHoldVariablePositionStrategy {
    public BuyAndHoldVariablePositionImplExponentialStdDev(PriceRepository priceRepository,
                                                           String asset,
                                                           int multiplier,
                                                           double allocatedCapital,
                                                           double targetRisk) {
        super(new NonNullPriceRepositoryImpl(priceRepository),
                asset,
                allocatedCapital,
                RiskTargetCalculator.builder()
                        .targetRisk(targetRisk)
                        .multiplier(multiplier)
                        .standardDeviation(new ExponentialStandardDeviation(priceRepository, asset))
                        .minimumContracts(4)
                        .build());
    }
}

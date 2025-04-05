package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.accounting.AccountingImpl;
import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.risktargetcalculator.RiskTargetCalculator;
import be.bavodaniels.finance.standarddeviation.ExponentialStandardDeviation;
import be.bavodaniels.finance.ta.EMAC;
import be.bavodaniels.finance.ta.indicator.EMA;


public class BuyAndHoldVariablePositionImplExponentialStdDev extends AbstractBuyAndHoldVariablePositionStrategy {
    public BuyAndHoldVariablePositionImplExponentialStdDev(PriceRepository priceRepository,
                                                           String asset,
                                                           int multiplier,
                                                           double allocatedCapital,
                                                           double targetRisk) {
        super(priceRepository,
                asset,
                allocatedCapital,
                RiskTargetCalculator.builder()
                        .targetRisk(targetRisk)
                        .multiplier(multiplier)
                        .standardDeviation(new ExponentialStandardDeviation(priceRepository, asset))
                        .minimumContracts(0)
                        .build(),
                new AccountingImpl(),
                new EMAC(new EMA(4, priceRepository, asset), new EMA(8, priceRepository, asset)));
    }
}

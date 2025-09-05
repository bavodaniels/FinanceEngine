package be.bavodaniels.finance.application;

import be.bavodaniels.finance.adapter.accounting.AccountingImpl;
import be.bavodaniels.finance.adapter.risktargetcalculator.DefaultRiskTargetCalculator;
import be.bavodaniels.finance.adapter.standarddeviation.ExponentialStandardDeviation;
import be.bavodaniels.finance.adapter.ta.EMAC;
import be.bavodaniels.finance.adapter.ta.indicator.EMA;
import be.bavodaniels.finance.core.strategy.AbstractBuyAndHoldVariablePositionStrategy;
import be.bavodaniels.finance.port.repository.PriceRepository;

public class BuyAndHoldVariablePositionImplExponentialStdDev extends AbstractBuyAndHoldVariablePositionStrategy {
    public BuyAndHoldVariablePositionImplExponentialStdDev(PriceRepository priceRepository,
                                                           String asset,
                                                           int multiplier,
                                                           double allocatedCapital,
                                                           double targetRisk) {
        super(priceRepository,
                asset,
                allocatedCapital,
                new DefaultRiskTargetCalculator(0.20, 5, new ExponentialStandardDeviation(priceRepository, asset)),
                new AccountingImpl(),
                new EMAC(new EMA(4, priceRepository, asset), new EMA(8, priceRepository, asset)));
    }
}

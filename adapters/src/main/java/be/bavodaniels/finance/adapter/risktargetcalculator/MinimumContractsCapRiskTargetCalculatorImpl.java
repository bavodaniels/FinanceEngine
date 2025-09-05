package be.bavodaniels.finance.adapter.risktargetcalculator;


import be.bavodaniels.finance.port.risktargetcalculator.RiskTargetCalculator;

import java.time.LocalDate;

public class MinimumContractsCapRiskTargetCalculatorImpl implements RiskTargetCalculator {
    private final RiskTargetCalculator delegate;
    private final int minimumToHold;

    public MinimumContractsCapRiskTargetCalculatorImpl(RiskTargetCalculator delegate, int minimumToHold) {
        this.delegate = delegate;
        this.minimumToHold = minimumToHold;
    }

    @Override
    public int calculateContractsToHold(Double allocatedCapital, Double underlyingPrice, LocalDate date) {
        int uncappedContractsToHold = delegate.calculateContractsToHold(allocatedCapital, underlyingPrice, date);

        return uncappedContractsToHold >= minimumToHold ? uncappedContractsToHold : 0;
    }
}

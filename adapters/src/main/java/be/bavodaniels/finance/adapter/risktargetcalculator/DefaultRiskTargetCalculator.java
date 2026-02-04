package be.bavodaniels.finance.adapter.risktargetcalculator;


import be.bavodaniels.finance.port.risktargetcalculator.RiskTargetCalculator;
import be.bavodaniels.finance.port.standarddeviation.StandardDeviation;

import java.time.LocalDate;

public class DefaultRiskTargetCalculator implements RiskTargetCalculator {

    private final double targetRisk;
    private final int multiplier;
    private final StandardDeviation stdDev;

    public DefaultRiskTargetCalculator(double targetRisk,
                                       int multiplier,
                                       StandardDeviation stdDev) {
        this.targetRisk = targetRisk;
        this.multiplier = multiplier;
        this.stdDev = stdDev;
    }

    public int calculateContractsToHold(Double allocatedCapital, Double underlyingPrice, LocalDate date) {
        if (allocatedCapital == null || underlyingPrice == null || date == null) {
            return 0;
        }

        double stdDevValue = stdDev.calculate(date);

        if (Double.isNaN(stdDevValue) || allocatedCapital == 0) {
            return 0;
        }

        if (underlyingPrice == 0 || stdDevValue == 0) {
            return Integer.MAX_VALUE;
        }

        double contractsFractional = (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stdDevValue);

        if (Double.isInfinite(contractsFractional) || Double.isNaN(contractsFractional)) {
            return contractsFractional > 0 ? Integer.MAX_VALUE : 0;
        }

        return Double.valueOf(contractsFractional).intValue();
    }
}

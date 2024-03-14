package be.bavodaniels.finance.risktargetcalculator;

import be.bavodaniels.finance.standarddeviation.StandardDeviation;

import java.time.LocalDate;

public class DefaultRiskTargetCalculator implements RiskTargetCalculator{

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
        double contractsFractional = (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stdDev.calculate(date));
        return Double.valueOf(contractsFractional).intValue();
    }
}
package be.bavodaniels.finance.strategy;

import be.bavodaniels.finance.standarddeviation.StandardDeviation;

import java.time.LocalDate;

public class ContractsToHoldCalculator {

    private final double targetRisk;
    private final int multiplier;
    private final int minimumContractsToHold;
    private final StandardDeviation stdDev;

    public ContractsToHoldCalculator(double targetRisk,
                                     int multiplier,
                                     int minimumContractsToHold,
                                     StandardDeviation stdDev) {
        this.targetRisk = targetRisk;
        this.multiplier = multiplier;
        this.minimumContractsToHold = minimumContractsToHold;
        this.stdDev = stdDev;
    }

    int calculateContractsToHold(Double allocatedCapital, Double underlyingPrice, LocalDate date) {
        double contractsFractional = (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stdDev.calculate(date));
        int wholeContracts = Double.valueOf(contractsFractional).intValue();
        return wholeContracts >= minimumContractsToHold ? wholeContracts : 0;
    }
}
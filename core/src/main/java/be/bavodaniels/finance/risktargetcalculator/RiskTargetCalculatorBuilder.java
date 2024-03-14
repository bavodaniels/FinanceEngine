package be.bavodaniels.finance.risktargetcalculator;

import be.bavodaniels.finance.standarddeviation.StandardDeviation;

public class RiskTargetCalculatorBuilder {
    private RiskTargetCalculator calculator;

    private Double targetRisk;
    private Integer multiplier;
    private StandardDeviation standardDeviation;
    private Integer minimalContracts;

    public RiskTargetCalculatorBuilder targetRisk(double targetRisk){
        this.targetRisk = targetRisk;
        return this;
    }

    public RiskTargetCalculatorBuilder multiplier(int multiplier){
        this.multiplier = multiplier;
        return this;
    }

    public RiskTargetCalculatorBuilder standardDeviation(StandardDeviation standardDeviation){
        this.standardDeviation = standardDeviation;
        return this;
    }

    public RiskTargetCalculatorBuilder minimumContracts(int minimalContracts){
        this.minimalContracts = minimalContracts;
        return this;
    }

    public RiskTargetCalculator build() {
        if (targetRisk == null ||
        multiplier == null ||
        standardDeviation == null){
            throw new RuntimeException("targetrisk, multiplier and standardDeviation should be filled in");
        }

        calculator = new DefaultRiskTargetCalculator(targetRisk, multiplier, standardDeviation);

        if (minimalContracts > 0){
            calculator = new MinimumContractsCapRiskTargetCalculatorImpl(calculator, minimalContracts);
        }

        return calculator;
    }
}

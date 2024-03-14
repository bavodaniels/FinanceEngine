package be.bavodaniels.finance.risktargetcalculator;

import java.time.LocalDate;

public interface RiskTargetCalculator {
    int calculateContractsToHold(Double allocatedCapital, Double underlyingPrice, LocalDate date);

    static RiskTargetCalculatorBuilder builder(){
        return new RiskTargetCalculatorBuilder();
    }
}

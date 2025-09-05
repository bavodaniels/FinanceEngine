package be.bavodaniels.finance.port.risktargetcalculator;

import java.time.LocalDate;

public interface RiskTargetCalculator {
    int calculateContractsToHold(Double allocatedCapital, Double underlyingPrice, LocalDate date);
}

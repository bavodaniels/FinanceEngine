package be.bavodaniels.finance.accounting.calculator;

import be.bavodaniels.finance.collection.StatisticalList;

import java.time.LocalDate;
import java.util.Map;

/**
 * Calculates return percentages based on price data, contracts held, and allocated capital.
 */
public class ReturnPercentageCalculator {

    private static final int CONTRACT_MULTIPLIER = 5;

    /**
     * Calculates the return percentage based on allocated capital and financial data.
     *
     * @param allocatedCapital the capital allocated to the strategy
     * @param backAdjustedPrices map of dates to back-adjusted prices
     * @param contractsHeld map of dates to number of contracts held
     * @return a StatisticalList containing the calculated return percentages
     */
    public StatisticalList calculate(double allocatedCapital, Map<LocalDate, Double> backAdjustedPrices, Map<LocalDate, Double> contractsHeld) {
        if (allocatedCapital <= 0) {
            throw new IllegalArgumentException("Allocated capital must be positive");
        }

        StatisticalList backAdjustedPricesList = new StatisticalList(backAdjustedPrices.values()).ffil();
        StatisticalList returnPricePoints = backAdjustedPricesList.difference().multiply(contractsHeld.values().stream().toList());
        StatisticalList returnBaseCurrency = returnPricePoints.multiply(CONTRACT_MULTIPLIER);

        return returnBaseCurrency.divide(allocatedCapital);
    }
}

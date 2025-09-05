package be.bavodaniels.finance.adapter.accounting.calculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Calculates turnover based on contracts held.
 */
public class TurnoverCalculator {

    private static final int TRADING_DAYS_PER_YEAR = 256;

    /**
     * Calculates the turnover based on contracts held.
     *
     * @param contractsHeld map of dates to number of contracts held
     * @return the calculated turnover
     */
    public double calculate(Map<LocalDate, Double> contractsHeld) {
        if (contractsHeld == null || contractsHeld.isEmpty()) {
            throw new IllegalArgumentException("Contracts held cannot be null or empty");
        }

        // Get the contracts held values as a list, preserving the order from the map
        List<Double> contractsHeldValues = new ArrayList<>(contractsHeld.values());

        if (contractsHeldValues.size() <= 1) {
            return 0.0; // Not enough data to calculate turnover
        }

        List<Double> differences = new ArrayList<>();

        // Calculate differences between consecutive elements
        for (int i = 1; i < contractsHeldValues.size(); i++) {
            Double currentValue = contractsHeldValues.get(i);
            Double previousValue = contractsHeldValues.get(i - 1);

            // Handle potential NaN values by skipping the difference calculation
            if (!Double.isNaN(currentValue) && !Double.isNaN(previousValue)) {
                Double difference = currentValue - previousValue;
                differences.add(difference);
            }
        }

        List<Double> percentageOfTrade = new ArrayList<>();
        for (int i = 1; i < contractsHeldValues.size(); i++) {
            double contractsHeldValue = contractsHeldValues.get(i);
            double difference = differences.get(i - 1);

            if (contractsHeldValue == 0.0) {
                percentageOfTrade.add(0.0);
            } else if (!Double.isNaN(contractsHeldValue) && !Double.isNaN(difference)) {
                percentageOfTrade.add(Math.abs(difference) / contractsHeldValue);
            }
        }

        double dailyTurnOver = percentageOfTrade.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);

        return dailyTurnOver * TRADING_DAYS_PER_YEAR;
    }
}

package be.bavodaniels.finance.adapter.accounting.calculator;


import be.bavodaniels.finance.adapter.collection.StatisticalList;

/**
 * Calculates drawdowns based on return percentages.
 */
public class DrawdownCalculator {

    /**
     * Calculates the drawdown based on return percentages.
     *
     * @param returnPercentage the return percentages to calculate drawdown from
     * @return a StatisticalList containing the calculated drawdowns
     */
    public StatisticalList calculate(StatisticalList returnPercentage) {
        if (returnPercentage == null || returnPercentage.isEmpty()) {
            throw new IllegalArgumentException("Return percentage cannot be null or empty");
        }

        StatisticalList cumSum = returnPercentage.cumSum();
        StatisticalList maxCumSum = cumSum.max();

        return maxCumSum.subtract(cumSum);
    }
}

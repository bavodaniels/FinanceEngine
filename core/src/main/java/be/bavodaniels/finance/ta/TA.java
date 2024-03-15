package be.bavodaniels.finance.ta;

import java.time.LocalDate;

public interface TA {
    /**
     * this calculates the confidence of a bull or bear market by returning a number between -20 (bearish) or +20 (bullish)
     * @param date
     * @return int between -20 and +20
     */
    int confidence(LocalDate date);
}

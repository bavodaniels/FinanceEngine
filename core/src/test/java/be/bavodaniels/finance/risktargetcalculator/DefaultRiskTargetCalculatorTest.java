package be.bavodaniels.finance.risktargetcalculator;

import be.bavodaniels.finance.standarddeviation.StandardDeviation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DefaultRiskTargetCalculatorTest {

    @Mock
    private StandardDeviation standardDeviation;

    private DefaultRiskTargetCalculator calculator;
    private final double targetRisk = 0.1;
    private final int multiplier = 5;
    private final LocalDate testDate = LocalDate.of(2023, 1, 15);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculator = new DefaultRiskTargetCalculator(targetRisk, multiplier, standardDeviation);
    }

    @Test
    void calculateContractsToHold_WithNormalValues_ReturnsExpectedContracts() {
        // Given
        double allocatedCapital = 10000.0;
        double underlyingPrice = 100.0;
        double stdDevValue = 0.2;

        when(standardDeviation.calculate(testDate)).thenReturn(stdDevValue);

        // Expected calculation: (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stdDev)
        // (10000 * 0.1) / (5 * 100 * 0.2) = 1000 / 100 = 10
        int expectedContracts = 10;

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(expectedContracts, result);
    }

    @Test
    void calculateContractsToHold_WithFractionalResult_ReturnsIntegerValue() {
        // Given
        double allocatedCapital = 10000.0;
        double underlyingPrice = 100.0;
        double stdDevValue = 0.15;

        when(standardDeviation.calculate(testDate)).thenReturn(stdDevValue);

        // Expected calculation: (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stdDev)
        // (10000 * 0.1) / (5 * 100 * 0.15) = 1000 / 75 = 13.33...
        // Should be converted to 13
        int expectedContracts = 13;

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(expectedContracts, result);
    }

    @Test
    void calculateContractsToHold_WithZeroStdDev_ReturnsInfinity() {
        // Given
        double allocatedCapital = 10000.0;
        double underlyingPrice = 100.0;
        double stdDevValue = 0.0;

        when(standardDeviation.calculate(testDate)).thenReturn(stdDevValue);

        // Expected calculation: (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stdDev)
        // (10000 * 0.1) / (5 * 100 * 0.0) = 1000 / 0 = Infinity
        // When converted to int, should be Integer.MAX_VALUE
        int expectedContracts = Integer.MAX_VALUE;

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(expectedContracts, result);
    }

    @Test
    void calculateContractsToHold_WithNaNStdDev_ReturnsZero() {
        // Given
        double allocatedCapital = 10000.0;
        double underlyingPrice = 100.0;

        when(standardDeviation.calculate(testDate)).thenReturn(Double.NaN);

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(0, result);
    }

    @Test
    void calculateContractsToHold_WithZeroUnderlyingPrice_ReturnsInfinity() {
        // Given
        double allocatedCapital = 10000.0;
        double underlyingPrice = 0.0;
        double stdDevValue = 0.2;

        when(standardDeviation.calculate(testDate)).thenReturn(stdDevValue);

        // Expected calculation: (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stdDev)
        // (10000 * 0.1) / (5 * 0.0 * 0.2) = 1000 / 0 = Infinity
        // When converted to int, should be Integer.MAX_VALUE
        int expectedContracts = Integer.MAX_VALUE;

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(expectedContracts, result);
    }

    @Test
    void calculateContractsToHold_WithZeroAllocatedCapital_ReturnsZero() {
        // Given
        double allocatedCapital = 0.0;
        double underlyingPrice = 100.0;
        double stdDevValue = 0.2;

        when(standardDeviation.calculate(testDate)).thenReturn(stdDevValue);

        // Expected calculation: (allocatedCapital * targetRisk) / (multiplier * underlyingPrice * stdDev)
        // (0.0 * 0.1) / (5 * 100 * 0.2) = 0 / 100 = 0
        int expectedContracts = 0;

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(expectedContracts, result);
    }

    @Test
    void calculateContractsToHold_WithNullInputs_ReturnsZero() {
        // Given
        Double allocatedCapital = null;
        Double underlyingPrice = null;
        LocalDate date = null;

        // When
        int resultNullAllocatedCapital = calculator.calculateContractsToHold(null, underlyingPrice, testDate);
        int resultNullUnderlyingPrice = calculator.calculateContractsToHold(allocatedCapital, null, testDate);
        int resultNullDate = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, null);

        // Then
        assertEquals(0, resultNullAllocatedCapital);
        assertEquals(0, resultNullUnderlyingPrice);
        assertEquals(0, resultNullDate);
    }
}

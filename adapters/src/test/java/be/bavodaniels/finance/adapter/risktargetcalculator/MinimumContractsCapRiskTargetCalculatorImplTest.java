package be.bavodaniels.finance.adapter.risktargetcalculator;

import be.bavodaniels.finance.port.risktargetcalculator.RiskTargetCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MinimumContractsCapRiskTargetCalculatorImplTest {

    @Mock
    private RiskTargetCalculator delegate;

    private MinimumContractsCapRiskTargetCalculatorImpl calculator;
    private final int minimumToHold = 5;
    private final LocalDate testDate = LocalDate.of(2023, 1, 15);
    private final Double allocatedCapital = 10000.0;
    private final Double underlyingPrice = 100.0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calculator = new MinimumContractsCapRiskTargetCalculatorImpl(delegate, minimumToHold);
    }

    @Test
    void calculateContractsToHold_WhenAboveMinimum_ReturnsOriginalValue() {
        // Given
        int originalContracts = 10; // Above minimum
        when(delegate.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate))
                .thenReturn(originalContracts);

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(originalContracts, result);
    }

    @Test
    void calculateContractsToHold_WhenEqualToMinimum_ReturnsOriginalValue() {
        // Given
        int originalContracts = minimumToHold; // Equal to minimum
        when(delegate.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate))
                .thenReturn(originalContracts);

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(originalContracts, result);
    }

    @Test
    void calculateContractsToHold_WhenBelowMinimum_ReturnsZero() {
        // Given
        int originalContracts = minimumToHold - 1; // Below minimum
        when(delegate.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate))
                .thenReturn(originalContracts);

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(0, result);
    }

    @Test
    void calculateContractsToHold_WhenNegative_ReturnsZero() {
        // Given
        int originalContracts = -1; // Negative value
        when(delegate.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate))
                .thenReturn(originalContracts);

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(0, result);
    }

    @Test
    void calculateContractsToHold_WithZeroMinimum_ReturnsOriginalValue() {
        // Given
        int zeroMinimum = 0;
        calculator = new MinimumContractsCapRiskTargetCalculatorImpl(delegate, zeroMinimum);
        int originalContracts = 3;

        when(delegate.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate))
                .thenReturn(originalContracts);

        // When
        int result = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, testDate);

        // Then
        assertEquals(originalContracts, result);
    }

    @Test
    void calculateContractsToHold_WithNullInputs_DelegatesToWrappedCalculator() {
        // Given
        Double nullAllocatedCapital = null;
        Double nullUnderlyingPrice = null;
        LocalDate nullDate = null;
        int delegateResult = 0; // Assuming delegate returns 0 for null inputs

        when(delegate.calculateContractsToHold(null, underlyingPrice, testDate)).thenReturn(delegateResult);
        when(delegate.calculateContractsToHold(allocatedCapital, null, testDate)).thenReturn(delegateResult);
        when(delegate.calculateContractsToHold(allocatedCapital, underlyingPrice, null)).thenReturn(delegateResult);

        // When
        int resultNullAllocatedCapital = calculator.calculateContractsToHold(null, underlyingPrice, testDate);
        int resultNullUnderlyingPrice = calculator.calculateContractsToHold(allocatedCapital, null, testDate);
        int resultNullDate = calculator.calculateContractsToHold(allocatedCapital, underlyingPrice, null);

        // Then
        assertEquals(delegateResult, resultNullAllocatedCapital);
        assertEquals(delegateResult, resultNullUnderlyingPrice);
        assertEquals(delegateResult, resultNullDate);
    }
}
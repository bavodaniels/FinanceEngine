package be.bavodaniels.finance.standarddeviation;

import be.bavodaniels.finance.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ExponentialStandardDeviationTest {

    @Mock
    private PriceRepository priceRepository;

    private ExponentialStandardDeviation standardDeviation;
    private final String asset = "TEST_ASSET";
    private final LocalDate testDate = LocalDate.of(2023, 1, 15);
    
    // Constants from ExponentialStandardDeviation
    private final int SHORT_LOOKBACK = 32;
    private final int LONG_LOOKBACK = 2600;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        standardDeviation = new ExponentialStandardDeviation(priceRepository, asset);
    }

    @Test
    void calculate_WithNormalData_ReturnsWeightedAverage() {
        // Given
        List<Double> shortPrices = Arrays.asList(100.0, 102.0, 101.0, 103.0, 105.0);
        List<Double> longPrices = Arrays.asList(90.0, 92.0, 94.0, 93.0, 95.0, 97.0);
        
        // Setup for short lookback
        when(priceRepository.getPricesFromDateUpUntilDate(
                eq(asset), 
                eq(testDate.minusDays(SHORT_LOOKBACK)), 
                eq(testDate)))
                .thenReturn(shortPrices);
        
        // Setup for long lookback
        when(priceRepository.getPricesFromDateUpUntilDate(
                eq(asset), 
                eq(testDate.minusDays(LONG_LOOKBACK)), 
                eq(testDate)))
                .thenReturn(longPrices);
        
        // Create the actual standard deviations that will be used internally
        NLookBackStandardDeviation shortStdDev = new NLookBackStandardDeviation(SHORT_LOOKBACK, priceRepository, asset);
        NLookBackStandardDeviation longStdDev = new NLookBackStandardDeviation(LONG_LOOKBACK, priceRepository, asset);
        
        // Calculate expected result
        double shortResult = shortStdDev.calculate(testDate);
        double longResult = longStdDev.calculate(testDate);
        double expected = (longResult * 0.3) + (shortResult * 0.7);
        
        // When
        double result = standardDeviation.calculate(testDate);
        
        // Then
        assertEquals(expected, result, 0.0001);
    }

    @Test
    void calculate_WithEmptyShortData_HandlesGracefully() {
        // Given
        List<Double> shortPrices = Collections.emptyList();
        List<Double> longPrices = Arrays.asList(90.0, 92.0, 94.0, 93.0, 95.0, 97.0);
        
        // Setup for short lookback
        when(priceRepository.getPricesFromDateUpUntilDate(
                eq(asset), 
                eq(testDate.minusDays(SHORT_LOOKBACK)), 
                eq(testDate)))
                .thenReturn(shortPrices);
        
        // Setup for long lookback
        when(priceRepository.getPricesFromDateUpUntilDate(
                eq(asset), 
                eq(testDate.minusDays(LONG_LOOKBACK)), 
                eq(testDate)))
                .thenReturn(longPrices);
        
        // When
        double result = standardDeviation.calculate(testDate);
        
        // Then - result should be NaN because short result is NaN
        assertEquals(Double.NaN, result, 0.0001);
    }

    @Test
    void calculate_WithEmptyLongData_HandlesGracefully() {
        // Given
        List<Double> shortPrices = Arrays.asList(100.0, 102.0, 101.0, 103.0, 105.0);
        List<Double> longPrices = Collections.emptyList();
        
        // Setup for short lookback
        when(priceRepository.getPricesFromDateUpUntilDate(
                eq(asset), 
                eq(testDate.minusDays(SHORT_LOOKBACK)), 
                eq(testDate)))
                .thenReturn(shortPrices);
        
        // Setup for long lookback
        when(priceRepository.getPricesFromDateUpUntilDate(
                eq(asset), 
                eq(testDate.minusDays(LONG_LOOKBACK)), 
                eq(testDate)))
                .thenReturn(longPrices);
        
        // When
        double result = standardDeviation.calculate(testDate);
        
        // Then - result should be NaN because long result is NaN
        assertEquals(Double.NaN, result, 0.0001);
    }

    @Test
    void calculate_WithBothEmptyData_ReturnsNaN() {
        // Given
        List<Double> emptyPrices = Collections.emptyList();
        
        // Setup for both lookbacks
        when(priceRepository.getPricesFromDateUpUntilDate(
                eq(asset), 
                any(LocalDate.class), 
                eq(testDate)))
                .thenReturn(emptyPrices);
        
        // When
        double result = standardDeviation.calculate(testDate);
        
        // Then
        assertEquals(Double.NaN, result, 0.0001);
    }
}
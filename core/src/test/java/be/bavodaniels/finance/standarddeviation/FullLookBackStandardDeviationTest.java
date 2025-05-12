package be.bavodaniels.finance.standarddeviation;

import be.bavodaniels.finance.collection.StatisticalList;
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
import static org.mockito.Mockito.when;

class FullLookBackStandardDeviationTest {

    @Mock
    private PriceRepository priceRepository;

    private FullLookBackStandardDeviation standardDeviation;
    private final String asset = "TEST_ASSET";
    private final LocalDate testDate = LocalDate.of(2023, 1, 15);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        standardDeviation = new FullLookBackStandardDeviation(priceRepository, asset);
    }

    @Test
    void calculate_WithNormalData_ReturnsExpectedStandardDeviation() {
        // Given
        List<Double> prices = Arrays.asList(100.0, 102.0, 101.0, 103.0, 105.0);
        
        when(priceRepository.getPricesUpUntilDate(asset, testDate))
                .thenReturn(prices);

        // Calculate expected standard deviation
        StatisticalList pricesList = new StatisticalList(prices);
        double expected = pricesList.pctChange().standardDeviation() * 16;

        // When
        double result = standardDeviation.calculate(testDate);

        // Then
        assertEquals(expected, result, 0.0001);
    }

    @Test
    void calculate_WithEmptyData_ReturnsNaN() {
        // Given
        List<Double> prices = Collections.emptyList();
        
        when(priceRepository.getPricesUpUntilDate(asset, testDate))
                .thenReturn(prices);

        // When
        double result = standardDeviation.calculate(testDate);

        // Then
        assertEquals(Double.NaN, result, 0.0001);
    }

    @Test
    void calculate_WithSingleDataPoint_ReturnsNaN() {
        // Given
        List<Double> prices = Collections.singletonList(100.0);
        
        when(priceRepository.getPricesUpUntilDate(asset, testDate))
                .thenReturn(prices);

        // When
        double result = standardDeviation.calculate(testDate);

        // Then
        assertEquals(Double.NaN, result, 0.0001);
    }

    @Test
    void calculate_WithNullData_ReturnsNaN() {
        // Given
        when(priceRepository.getPricesUpUntilDate(asset, testDate))
                .thenReturn(null);

        // When
        double result = standardDeviation.calculate(testDate);

        // Then
        assertEquals(Double.NaN, result, 0.0001);
    }
}
package be.bavodaniels.finance.collection;

import org.junit.jupiter.api.Test;
import tech.tablesaw.api.DoubleColumn;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticalListTest {
    public final List<Double> values = List.of(0.346196618,
            0.636060665,
            0.668699073,
            0.02965205,
            0.057669718,
            0.103328302,
            0.192976179,
            0.219468587,
            0.319558773,
            0.515297289,
            0.242460936,
            0.110632853,
            0.073120998,
            0.463662153,
            0.85088375,
            0.892163073,
            0.595035933,
            0.015693837,
            0.28651444,
            0.116821126,
            0.170384359,
            0.646843092,
            0.023601704,
            0.666284142,
            0.511504277,
            0.948432335,
            0.551966915,
            0.94919339,
            0.206134435,
            0.083638134);

    @Test
    void testStandardDeviation() {
        StatisticalList list = new StatisticalList(values);

        DoubleColumn column = DoubleColumn.create("test", list);

        assertEquals(column.standardDeviation(), list.standardDeviation());
    }

    @Test
    void testDifference() {
        StatisticalList list = new StatisticalList(values);
        DoubleColumn column = DoubleColumn.create("test", list);

        DoubleColumn returnPricePoints = DoubleColumn.create("returnPricePoints")
                .append(DoubleColumn.create("priceChange", list)
                        .subtract(column.lag(1)));
        List<Double> expected = returnPricePoints.asList();
        expected.removeFirst();
        expected.addFirst(0.0);
        assertEquals(expected, list.difference().stream().toList());
    }
}
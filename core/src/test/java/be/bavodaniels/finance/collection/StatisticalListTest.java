package be.bavodaniels.finance.collection;

import org.junit.jupiter.api.Test;

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

        assertEquals(0.29973875178242215, list.standardDeviation());
    }

    @Test
    void testDifference() {
        StatisticalList list = new StatisticalList(values);
        List<Double> expected = List.of(0.0, 0.289864047, 0.03263840799999995, -0.6390470229999999, 0.028017668000000003, 0.045658583999999995, 0.089647877, 0.026492407999999995, 0.10009018600000003, 0.19573851600000003, -0.2728363530000001, -0.13182808299999998, -0.037511855, 0.39054115500000003, 0.38722159700000003, 0.04127932299999992, -0.29712713999999996, -0.579342096, 0.270820603, -0.16969331399999998, 0.053563233000000016, 0.47645873299999997, -0.6232413880000001, 0.642682438, -0.15477986499999996, 0.436928058, -0.39646542000000007, 0.3972264750000001, -0.743058955, -0.122496301);
        assertEquals(expected, list.difference().stream().toList());
    }


    @Test
    void testPctChange() {
        StatisticalList list = new StatisticalList(values);
        List<Double> expectedPctChange = List.of(
                0.8372815675513041, 0.05131335703647055, -0.9556571091582775, 0.9448813151198654, 0.7917254597985028, 0.8676023438379932, 0.13728330686866785, 0.4560570028183579, 0.6125274363849182, -0.5294736821330337, -0.5437085461057529, -0.33906614520733724, 5.341026048358913, 0.8351373828866295, 0.04851346967197334, -0.3330412891904123, -0.9736253961657808, 17.256493934529843, -0.5922679289741906, 0.45850639207158483, 2.7963760042082266, -0.9635124742122159, 27.230340572019713, -0.2323030899930978, 0.8542021594865374, -0.4180218296753875, 0.7196563130962299, -0.782831994858287, -0.5942544291544496
        );
        assertEquals(expectedPctChange, list.pctChange().stream().toList());
    }

    @Test
    void testMultiplySingleNumber() {
        StatisticalList list = new StatisticalList(List.of(1.0, 2.0, 3.0));
        assertEquals(List.of(2.0,4.0,6.0), list.multiply(2).stream().toList());
    }

    @Test
    void testDivideSingleNumber() {
        StatisticalList list = new StatisticalList(List.of(1.0, 2.0, 3.0));
        assertEquals(List.of(0.5,1.0,1.5), list.divide(2).stream().toList());
    }

    @Test
    void testMean() {
        StatisticalList list = new StatisticalList(values);
        assertEquals(0.3831293045333333, list.mean());
    }
    @Test
    void testMax() {
        StatisticalList list = new StatisticalList(values);
        List<Double> expected = List.of(0.0, 0.636060665, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.668699073, 0.85088375, 0.892163073, 0.892163073, 0.892163073, 0.892163073, 0.892163073, 0.892163073, 0.892163073, 0.892163073, 0.892163073, 0.892163073, 0.948432335, 0.948432335, 0.94919339, 0.94919339, 0.94919339);
        assertEquals(expected , list.max());
    }
}
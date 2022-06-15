package be.bavodaniels.core.statistics;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StandardDeviation {
    private final Average averageStat = new Average();
    public Optional<Double> calculate(List<Double> data) {
        var average = averageStat.calculate(data);

        return average.flatMap(aDouble -> data.stream()
                .map(e -> e - aDouble)
                .map(e -> Math.pow(e, 2))
                .reduce(Double::sum)
                .map(sum -> sum / (data.size() - 1))
                .map(Math::sqrt));
    }
}

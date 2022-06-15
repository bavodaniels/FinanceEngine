package be.bavodaniels.core.ta;

import be.bavodaniels.core.statistics.Max;
import be.bavodaniels.core.statistics.Min;

import java.util.List;
import java.util.Optional;

public class Breakout {
    private static final Min minStat = new Min();
    private static final Max maxStat = new Max();

    /**
     * @param data List with ticker data order by newest data first
     * @param period the amount of days to calculate the breakout for
     * @return the moving average
     */
    public Optional<Result> calculate(List<Double> data, int period){
        if (period > data.size())
            return Optional.empty();

        List<Double> neededData = data.subList(0, period);

        Optional<Double> min = minStat.calculate(neededData);

        Optional<Double> max = maxStat.calculate(neededData);

        return min.map(aDouble -> new Result(aDouble, max.get()));
    }

    public record Result(double lower, double upper){
    }
}

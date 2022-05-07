package be.bavodaniels.core.ta;

import be.bavodaniels.core.ta.exception.NotEnoughDataException;

import java.util.List;
import java.util.Optional;

public class Breakout {

    /**
     * @param data List with ticker data order by newest data first
     * @param period the amount of days to calculate the breakout for
     * @return the moving average
     */
    public Optional<Result> calculate(List<Double> data, int period){
        if (period > data.size())
            throw new NotEnoughDataException();

        List<Double> neededData = data.subList(0, period);

        Optional<Double> min = neededData.stream()
                .min(Double::compareTo);

        Optional<Double> max = neededData.stream()
                .max(Double::compareTo);

        return min.map(aDouble -> new Result(aDouble, max.get()));
    }

    public record Result(double lower, double upper){
    }
}

package be.bavodaniels.core.ta;

import be.bavodaniels.core.ta.exception.NotEnoughDataException;

import java.awt.geom.Arc2D;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

public class MovingAverage {

    /**
     * @param data List with ticker data order by newest data first
     * @param period the amount of days to calculate the moving average for
     * @return the moving average
     */
    public Optional<Double> calculate(List<Double> data, int period){
        if (period > data.size())
            throw new NotEnoughDataException();

        List<Double> neededData = data.subList(0, period);

        return neededData.stream().reduce(Double::sum)
                .map(sum -> sum / period);
    }
}

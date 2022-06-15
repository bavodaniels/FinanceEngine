package be.bavodaniels.core.ta;


import be.bavodaniels.core.statistics.Average;

import java.awt.geom.Arc2D;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

public class MovingAverage {

    public static final Average average = new Average();

    public Optional<Double> calculate(List<Double> data) {
        return average.calculate(data);
    }
}

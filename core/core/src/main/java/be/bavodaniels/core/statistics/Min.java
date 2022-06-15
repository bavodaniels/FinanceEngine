package be.bavodaniels.core.statistics;

import java.util.List;
import java.util.Optional;

public class Min {
    public Optional<Double> calculate(List<Double> data){
        return data.stream().min(Double::compare);
    }
}

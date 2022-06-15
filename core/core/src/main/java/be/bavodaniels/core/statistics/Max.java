package be.bavodaniels.core.statistics;

import java.util.List;
import java.util.Optional;

public class Max {
    public Optional<Double> calculate(List<Double> data){
        return data.stream().max(Double::compare);
    }
}

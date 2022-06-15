package be.bavodaniels.core.statistics;

import java.util.List;
import java.util.Optional;

public class Average {

    public Optional<Double> calculate(List<Double> data){
        return data.stream().reduce(Double::sum)
                .map(sum -> sum / data.size());
    }
}

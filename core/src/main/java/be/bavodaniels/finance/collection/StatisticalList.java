package be.bavodaniels.finance.collection;

import org.apache.commons.math4.legacy.stat.StatUtils;
import org.apache.commons.math4.legacy.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math4.legacy.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticalList extends ArrayList<Double> {
    public StatisticalList(Collection<Double> c) {
        super(c);
    }

    public StatisticalList pctChange() {
        List<Double> values = this.stream().toList();
        List<Double> shifted = new LinkedList<>(values);
        shifted.removeFirst();
        List<java.lang.Double> pctChange = new ArrayList<>();
        for (int i = 0; i < shifted.size(); i++) {

            double previousValue = values.get(i);
            double currentValue = shifted.get(i);
            double currentPctChange = (currentValue / previousValue) - 1;
            pctChange.add(currentPctChange);
        }
        return new StatisticalList(pctChange);
    }


    /**
     * @return a {@link StatisticalList} with the difference with the previous value
     */
    public StatisticalList difference() {
        List<Double> values = this.stream().toList();
        List<Double> shifted = new LinkedList<>(values);
        shifted.removeFirst();
        List<Double> difference = new ArrayList<>();
        difference.add(0.0);
        for (int i = 0; i < shifted.size(); i++) {
            Double previousValue = values.get(i);
            Double currentValue = shifted.get(i);
            Double currentPctChange = currentValue - previousValue;
            difference.add(currentPctChange);
        }
        return new StatisticalList(difference);
    }

    public Double standardDeviation() {
        double[] values = this.parallelStream().mapToDouble(value -> value).toArray();
        return new StandardDeviation(true).evaluate(values, StatUtils.mean(values));
    }

    public StatisticalList multiply(List<Double> values) {
        StatisticalList output = new StatisticalList(new ArrayList<>());
        for (int i = 0; i < this.size(); i++) {
            output.add(this.get(i) * values.get(i));
        }
        return output;
    }

    public StatisticalList multiply(int i) {
        return new StatisticalList(this.parallelStream().map(value -> value * i).collect(Collectors.toList()));
    }

    public StatisticalList divide(double i) {
        return new StatisticalList(this.parallelStream().map(value -> value / i).collect(Collectors.toList()));
    }

    public StatisticalList cumSum() {
        List<Double> list = new ArrayList<>();

        list.add(0.0);
        for (int i = 1; i < this.size(); i++) {
            list.add(list.get(i - 1) + this.get(i));
        }
        return new StatisticalList(list);
    }

    public Double mean() {
        return new DescriptiveStatistics(this.toArray(new Double[0])).getMean();
    }

    public StatisticalList max() {
        List<Double> output = new ArrayList<>();
        output.add(0.0);
        for (int i = 1; i < this.size(); i++) {
            output.add(Math.max(output.get(i - 1), this.get(i)));
        }
        return new StatisticalList(output);
    }

    public StatisticalList subtract(StatisticalList cumSum) {
        List<Double> output = new ArrayList<>();
        for (int i = 0; i < this.size(); i++) {
            output.add(this.get(i) - cumSum.get(i));
        }

        return new StatisticalList(output);
    }

    public StatisticalList subtract(double mean) {
        return new StatisticalList(this.parallelStream().map(value -> value - mean).collect(Collectors.toList()));
    }

    public double skewness() {
        return new DescriptiveStatistics(this.toArray(new Double[0])).getSkewness();
    }

    public StatisticalList ffil() {
        Double[] values = this.toArray(new Double[0]);
        for (int i = 0; i < values.length; i++) {
            if (values[i].isNaN()) {
                values[i] = values[i -1];
            }
        }
        return new StatisticalList(List.of(values));
    }

    public StatisticalList shift(){
        this.remove(0);
        return this;
    }
}

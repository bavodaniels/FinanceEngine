package be.bavodaniels.finance.repository;

import com.opencsv.bean.CsvBindByName;

public class TimeSeriesEntry {
    @CsvBindByName(column = "index")
    private String date;
    @CsvBindByName(column = "adjusted")
    private double adjusted;
    @CsvBindByName(column = "underlying")
    private double underlying;

    public TimeSeriesEntry(String date,
                           double adjusted,
                           double underlying) {
        this.date = date;
        this.adjusted = adjusted;
        this.underlying = underlying;
    }

    public TimeSeriesEntry() {
    }

    public String getDate() {
        return date;
    }

    public double getAdjusted() {
        return adjusted;
    }

    public double getUnderlying() {
        return underlying;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAdjusted(double adjusted) {
        this.adjusted = adjusted;
    }

    public void setUnderlying(double underlying) {
        this.underlying = underlying;
    }
}

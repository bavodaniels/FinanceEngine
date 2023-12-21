package be.bavodaniels.finance.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class StockId implements Serializable {
    private static final long serialVersionUID = -7524885844376155761L;
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "symbol")
    private String symbol;

    public StockId() {
    }

    public StockId(LocalDate date, String symbol) {
        this.date = date;
        this.symbol = symbol;
    }

    public LocalDate getDate() {
        return date;
    }

    public StockId setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public StockId setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StockId entity = (StockId) o;
        return Objects.equals(this.date, entity.date) &&
                Objects.equals(this.symbol, entity.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, symbol);
    }

}
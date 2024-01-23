package be.bavodaniels.finance.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity(name = "Stock")
@Table(name = "stocks")
public class Stock {
    @Id
    private Integer id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "backadjusted_price")
    private Double backadjustedPrice;

    @Column(name = "underlying_price")
    private Double underlyingPrice;

    public Stock() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getBackadjustedPrice() {
        return backadjustedPrice;
    }

    public void setBackadjustedPrice(Double backadjustedPrice) {
        this.backadjustedPrice = backadjustedPrice;
    }

    public Double getUnderlyingPrice() {
        return underlyingPrice;
    }

    public void setUnderlyingPrice(Double underlyingPrice) {
        this.underlyingPrice = underlyingPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock stock)) return false;

        if (getId() != null ? !getId().equals(stock.getId()) : stock.getId() != null) return false;
        if (getDate() != null ? !getDate().equals(stock.getDate()) : stock.getDate() != null) return false;
        if (getSymbol() != null ? !getSymbol().equals(stock.getSymbol()) : stock.getSymbol() != null) return false;
        if (getBackadjustedPrice() != null ? !getBackadjustedPrice().equals(stock.getBackadjustedPrice()) : stock.getBackadjustedPrice() != null)
            return false;
        return getUnderlyingPrice() != null ? getUnderlyingPrice().equals(stock.getUnderlyingPrice()) : stock.getUnderlyingPrice() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getSymbol() != null ? getSymbol().hashCode() : 0);
        result = 31 * result + (getBackadjustedPrice() != null ? getBackadjustedPrice().hashCode() : 0);
        result = 31 * result + (getUnderlyingPrice() != null ? getUnderlyingPrice().hashCode() : 0);
        return result;
    }
}
package be.bavodaniels.finance.repository.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity(name = "Stock")
@Table(name = "stocks")
public class Stock {
    @EmbeddedId
    private StockId id;

    @Column(name = "backadjusted_price")
    private Double backadjustedPrice;

    @Column(name = "underlying_price")
    private Double underlyingPrice;

    public StockId getId() {
        return id;
    }

    public Stock setId(StockId id) {
        this.id = id;
        return this;
    }

    public Double getBackadjustedPrice() {
        return backadjustedPrice;
    }

    public Stock setBackadjustedPrice(Double backadjustedPrice) {
        this.backadjustedPrice = backadjustedPrice;
        return this;
    }

    public Double getUnderlyingPrice() {
        return underlyingPrice;
    }

    public Stock setUnderlyingPrice(Double underlyingPrice) {
        this.underlyingPrice = underlyingPrice;
        return this;
    }

}
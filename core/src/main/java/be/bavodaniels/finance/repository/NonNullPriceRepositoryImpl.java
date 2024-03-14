package be.bavodaniels.finance.repository;

import java.time.LocalDate;
import java.util.List;

public class NonNullPriceRepositoryImpl implements PriceRepository {
    private final PriceRepository delegate;

    public NonNullPriceRepositoryImpl(PriceRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public Double getPrice(String asset, LocalDate date) {
        Double price = delegate.getPrice(asset, date);

        int daysToSubstract = 1;
        while (price == null) {
            price = delegate.getPrice(asset, date.minusDays(daysToSubstract));
            daysToSubstract += 1;
        }
        return price;
    }

    @Override
    public Double getUnderlyingPrice(String asset, LocalDate date) {
        Double price = delegate.getUnderlyingPrice(asset, date);

        int daysToSubstract = 1;
        while (price == null) {
            price = delegate.getUnderlyingPrice(asset, date.minusDays(daysToSubstract));
            daysToSubstract += 1;
        }
        return price;
    }

    @Override
    public List<Double> getPricesUpUntilDate(String asset, LocalDate date) {
        return delegate.getPricesUpUntilDate(asset, date);
    }

    @Override
    public List<Double> getPricesFromDataUpUntilDate(String asset, LocalDate from, LocalDate to) {
        return delegate.getPricesFromDataUpUntilDate(asset, from, to);
    }
}

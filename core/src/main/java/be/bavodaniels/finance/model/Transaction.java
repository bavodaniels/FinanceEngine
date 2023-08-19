package be.bavodaniels.finance.model;

import java.time.LocalDate;

public record Transaction(LocalDate date,
                          double price,
                          int amount,
                          TransactionType type) implements Comparable<Transaction> {
    @Override
    public int compareTo(Transaction o) {
        return this.date.compareTo(o.date());
    }
}

package be.bavodaniels.core.service;

import be.bavodaniels.ticker.TickerRepository;

public class FinanceService {
    private TickerRepository repository;

    public FinanceService(TickerRepository repository) {
        this.repository = repository;
    }
}


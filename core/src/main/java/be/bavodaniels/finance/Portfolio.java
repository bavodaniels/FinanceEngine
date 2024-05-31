package be.bavodaniels.finance;

import be.bavodaniels.finance.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private List<FuturesContract> holdings = new ArrayList<>();
    private double capital;
    private Strategy strategy;

    public Portfolio(double capital, Strategy strategy) {

        this.capital = capital;
        this.strategy = strategy;
    }
}

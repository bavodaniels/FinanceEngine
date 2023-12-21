package be.bavodaniels.finance.strategy;

import java.time.LocalDate;
import java.util.List;

public class FixedPortfolioStrategyImpl implements Strategy {
    private final List<Strategy> subStrategies;

    public FixedPortfolioStrategyImpl(Strategy... strategies) {
        subStrategies = List.of(strategies);
    }

    @Override
    public void run(LocalDate date) {
        subStrategies.forEach(strategy -> strategy.run(date));
    }

    @Override
    public void sellAll(LocalDate date) {
        subStrategies.forEach(strategy -> strategy.sellAll(date));
    }

    @Override
    public Statistics getStatistics() {
        return null;
    }
}

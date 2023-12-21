package be.bavodaniels.finance;

import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.strategy.BuyAndHoldStrategySingleContractImpl;
import be.bavodaniels.finance.strategy.Statistics;
import be.bavodaniels.finance.strategy.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.logging.Logger;

@Component
public class BackTestStrategy1 {
    private final Logger logger = Logger.getLogger("BackTest");
    public Strategy strategy;
    private final PriceRepository repository;

    @Autowired
    public BackTestStrategy1(PriceRepository priceRepository) {
        this.repository = priceRepository;
    }

    public void run(){
        this.strategy = new BuyAndHoldStrategySingleContractImpl(repository, "sp500", 5);

        LocalDate startDate = LocalDate.parse("1982-11-14");
        LocalDate endDate = LocalDate.parse("2022-09-30");

        while (startDate.isBefore(endDate)){
            strategy.run(startDate);
            startDate = startDate.plusDays(1L);
        }

        strategy.sellAll(endDate);

        Statistics stats = strategy.getStatistics();

        System.out.println("Strategy1 " + stats);
    }
}

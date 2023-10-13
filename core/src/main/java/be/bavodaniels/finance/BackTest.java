package be.bavodaniels.finance;

import be.bavodaniels.finance.repository.CsvPriceRepositoryImpl;
import be.bavodaniels.finance.strategy.BuyAndHoldStrategySingleContractImpl;
import be.bavodaniels.finance.strategy.Statistics;
import be.bavodaniels.finance.strategy.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.logging.Logger;

@Component
public class BackTest {
    private final Logger logger = Logger.getLogger("BackTest");
    public Strategy strategy;

    @Autowired
    public BackTest(CsvPriceRepositoryImpl priceRepository) {
        this.strategy = new BuyAndHoldStrategySingleContractImpl(priceRepository, "sp500", 5);

        LocalDate startDate = LocalDate.parse("1982-11-14");
        LocalDate endDate = LocalDate.parse("2022-09-30");

        logger.info("years of data " + (endDate.toEpochDay() - startDate.toEpochDay()) / 306.25);

        while (startDate.isBefore(endDate)){
            strategy.run(startDate);
            startDate = startDate.plusDays(1L);
        }

        strategy.sellAll(endDate);

        Statistics stats = strategy.getStatistics();

        System.out.println(stats);
    }
}
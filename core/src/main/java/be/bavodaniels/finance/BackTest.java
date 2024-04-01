package be.bavodaniels.finance;

import be.bavodaniels.finance.repository.PriceRepository;
import be.bavodaniels.finance.strategy.BuyAndHoldVariablePositionImplExponentialStdDev;
import be.bavodaniels.finance.strategy.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class BackTest {
    @Autowired
    public BackTest(PriceRepository repository) throws InterruptedException {

        List<Thread> backtests = new ArrayList<>();

        Strategy stratsp500 = new BuyAndHoldVariablePositionImplExponentialStdDev(repository, "sp500", 5, 100000.0, 0.20);

        backtests.add(Thread.startVirtualThread(() -> runStrat(stratsp500)));

        for (Thread t : backtests) {
            t.join();
        }
    }

    private void runStrat(Strategy strategy){
        LocalDate startDate = LocalDate.parse("1997-01-02");
        LocalDate endDate = LocalDate.parse("2022-09-30");

        while (startDate.isBefore(endDate)){
            try {
                strategy.run(startDate);
            }catch(RuntimeException e){
                //do nothing
            }
            startDate = startDate.plusDays(1L);

        }

        System.out.println(strategy.getStatistics());
    }
}

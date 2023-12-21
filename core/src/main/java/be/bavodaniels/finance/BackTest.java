package be.bavodaniels.finance;

import be.bavodaniels.finance.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BackTest {
    @Autowired
    public BackTest(BackTestStrategy1 strategy1,
                    BackTestStrategy2_FullDataSet strategy2FullDataSet,
                    BackTestStrategy2_FullLookBack strategy2FullLookBack,
                    BackTestStrategy2_NLookBack strategy2NLookBack,
                    BackTestStrategy3_ExponentialStandardDeviation strategy3Exp,
                    PriceRepository repository) throws InterruptedException {

        List<Thread> backtests = new ArrayList<>();

//        repository.findAll("sp500");
//        repository.findAll("us10");

        backtests.add(Thread.startVirtualThread(strategy1::run));
        backtests.add(Thread.startVirtualThread(strategy2FullDataSet::run));
        backtests.add(Thread.startVirtualThread(strategy2FullLookBack::run));
        backtests.add(Thread.startVirtualThread(strategy2NLookBack::run));
        backtests.add(Thread.startVirtualThread(strategy3Exp::run));

        for (Thread t : backtests) {
            t.join();
        }
    }
}

package be.bavodaniels.finance;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BackTest2 {
    private static final List<String> assets = List.of("sp500", "us10");

    public BackTest2() {
        LocalDate startDate = LocalDate.parse("1982-11-14");
        LocalDate endDate = LocalDate.parse("2022-09-30");


    }
}

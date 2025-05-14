package be.bavodaniels.finance.accounting;

import be.bavodaniels.finance.strategy.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class AccountingImplTest {
    private AccountingImpl accounting = new AccountingImpl();
    private Statistics statistics;

    @BeforeEach
    void setUp() {
        accounting.register(LocalDate.parse("1982-09-14"),405.4,405.4,175.2940865);
        accounting.register(LocalDate.parse("1982-09-15"),405.9,405.9,174.5883938);
        accounting.register(LocalDate.parse("1982-09-16"),405.25,405.25,175.5069082);
        accounting.register(LocalDate.parse("1982-09-17"),404.35,404.35,176.7947697);
        accounting.register(LocalDate.parse("1982-09-20"),405.15,405.15,175.6490766);
        accounting.register(LocalDate.parse("1982-09-21"),408.05,408.05,171.6175584);
        accounting.register(LocalDate.parse("1982-09-22"),403.9,403.9,177.4458143);
        accounting.register(LocalDate.parse("1982-09-23"),405.5,405.5,175.152492);
        accounting.register(LocalDate.parse("1982-09-24"),404.65,404.65,176.3633876);
        accounting.register(LocalDate.parse("1982-09-27"),405.75,405.75,174.7995043);
        accounting.register(LocalDate.parse("1982-09-28"),404.05,404.05,177.2282673);
        accounting.register(LocalDate.parse("1982-09-29"),401.65,401.65,180.7743102);
        accounting.register(LocalDate.parse("1982-09-30"),401.05,401.05,181.6831044);
        accounting.register(LocalDate.parse("1982-10-01"),401.85,401.85,180.4733958);
        accounting.register(LocalDate.parse("1982-10-04"),402.4,402.4,179.6510232);
        accounting.register(LocalDate.parse("1982-10-05"),402.25,402.25,179.8745625);
        accounting.register(LocalDate.parse("1982-10-06"),407.25,407.25,172.7110992);
        accounting.register(LocalDate.parse("1982-10-07"),411.85,411.85,166.6068268);
        accounting.register(LocalDate.parse("1982-10-08"),413.8,413.8,164.1474527);
        accounting.register(LocalDate.parse("1982-10-11"),417.5,417.5,159.6750994);
        accounting.register(LocalDate.parse("1982-10-12"),415.75,415.75,161.7596308);
        accounting.register(LocalDate.parse("1982-10-13"),419.35,419.35,157.5290847);
        accounting.register(LocalDate.parse("1982-10-14"),416.65,416.65,160.6808337);
        accounting.register(LocalDate.parse("1982-10-15"),417.0,417.0,160.2651774);
        accounting.register(LocalDate.parse("1982-10-18"),420.4,420.4,156.3365429);
        accounting.register(LocalDate.parse("1982-10-19"),419.1,419.1,157.8157096);
        accounting.register(LocalDate.parse("1982-10-20"),423.15,423.15,153.2971262);
        accounting.register(LocalDate.parse("1982-10-21"),422.8,422.8,153.6773813);
        accounting.register(LocalDate.parse("1982-10-22"),420.15,420.15,156.6188408);
        accounting.register(LocalDate.parse("1982-10-25"),415.15,415.15,162.4869127);
        accounting.register(LocalDate.parse("1982-10-26"),417.95,417.95,159.1477321);
        accounting.register(LocalDate.parse("1982-10-27"),416.9,416.9,160.3837168);
        accounting.register(LocalDate.parse("1982-10-28"),415.6,415.6,161.9408402);
        accounting.register(LocalDate.parse("1982-10-29"),416.25,416.25,161.1585173);
        accounting.register(LocalDate.parse("1982-11-01"),419.15,419.15,157.7583012);
        accounting.register(LocalDate.parse("1982-11-02"),420.55,420.55,156.1676522);
        accounting.register(LocalDate.parse("1982-11-03"),425.55,425.55,150.7395099);
        accounting.register(LocalDate.parse("1982-11-04"),425.75,425.75,150.5302222);
        accounting.register(LocalDate.parse("1982-11-05"),424.9,424.9,151.4237326);
        accounting.register(LocalDate.parse("1982-11-08"),423.9,423.9,152.4885971);
        accounting.register(LocalDate.parse("1982-11-09"),427.5,427.5,148.7234465);
        accounting.register(LocalDate.parse("1982-11-10"),423.4,423.4,153.0266655);
        accounting.register(LocalDate.parse("1982-11-11"),425.85,425.85,150.4257961);
        accounting.register(LocalDate.parse("1982-11-12"),422.5,422.5,154.0048189);
        accounting.register(LocalDate.parse("1982-11-15"),418.6,418.6,158.3921001);
        accounting.register(LocalDate.parse("1982-11-16"),418.4,418.4,158.6238369);
        accounting.register(LocalDate.parse("1982-11-17"),420.4,420.4,156.3365429);
        accounting.register(LocalDate.parse("1982-11-18"),420.9,420.9,155.7749893);
        accounting.register(LocalDate.parse("1982-11-19"),417.9,417.9,159.2061564);
        accounting.register(LocalDate.parse("1982-11-22"),414.4,414.4,163.4052638);
        accounting.register(LocalDate.parse("1982-11-23"),414.25,414.25,163.5901811);
        accounting.register(LocalDate.parse("1982-11-24"),415.5,415.5,162.0618722);
        accounting.register(LocalDate.parse("1982-11-26"),416.65,416.65,162.0618722);
        accounting.register(LocalDate.parse("1982-11-29"),415.35,415.35,160.6808337);
        accounting.register(LocalDate.parse("1982-11-30"),420.35,420.35,162.2437599);
        accounting.register(LocalDate.parse("1982-12-01"),419.8,419.8,156.3929211);
        accounting.register(LocalDate.parse("1982-12-02"),420.55,420.55,157.0157748);
        accounting.register(LocalDate.parse("1982-12-03"),420.85,420.85,156.1676522);
        accounting.register(LocalDate.parse("1982-12-06"),425.05,425.05,155.830963);
        accounting.register(LocalDate.parse("1982-12-07"),425.45,425.45,151.2652843);
        accounting.register(LocalDate.parse("1982-12-08"),423.05,423.05,150.8443722);
        accounting.register(LocalDate.parse("1982-12-09"),421.4,421.4,153.4055784);
        accounting.register(LocalDate.parse("1982-12-10"),420.95,420.95,155.2174553);
        accounting.register(LocalDate.parse("1982-12-13"),422.2,422.2,155.7190557);
        accounting.register(LocalDate.parse("1982-12-14"),417.5,417.5,154.3336548);
        accounting.register(LocalDate.parse("1982-12-15"),417.0,417.0,158.798085);
        accounting.register(LocalDate.parse("1982-12-16"),416.9,416.9,159.3816869);
        accounting.register(LocalDate.parse("1982-12-17"),419.5,419.5,159.4989224);
        accounting.register(LocalDate.parse("1982-12-20"),417.8,417.8,156.5057994);
        accounting.register(LocalDate.parse("1982-12-21"),421.8,421.8,158.4499708);
        accounting.register(LocalDate.parse("1982-12-22"),420.9,420.9,153.9501491);
        accounting.register(LocalDate.parse("1982-12-23"),421.7,421.7,154.9401822);
        accounting.register(LocalDate.parse("1982-12-27"),425.65,425.65,154.0595276);
        accounting.register(LocalDate.parse("1982-12-28"),423.2,423.2,154.0595276);
        accounting.register(LocalDate.parse("1982-12-29"),423.95,423.95,149.8540325);
        accounting.register(LocalDate.parse("1982-12-30"),422.1,422.1,152.4349983);
        accounting.register(LocalDate.parse("1982-12-31"),422.75,422.75,151.635514);
        accounting.register(LocalDate.parse("1983-01-03"),419.95,419.95,153.6229437);
        accounting.register(LocalDate.parse("1983-01-04"),423.45,423.45,152.9187483);
        accounting.register(LocalDate.parse("1983-01-05"),423.85,423.85,155.9991259);
        accounting.register(LocalDate.parse("1983-01-06"),427.9,427.9,152.1675685);
        accounting.register(LocalDate.parse("1983-01-07"),427.95,427.95,151.741627);
        accounting.register(LocalDate.parse("1983-01-10"),429.65,429.65,147.5595679);
        accounting.register(LocalDate.parse("1983-01-11"),428.85,428.85,147.5093776);
        accounting.register(LocalDate.parse("1983-01-12"),428.9,428.9,145.8229893);
        accounting.register(LocalDate.parse("1983-01-13"),429.05,429.05,146.6117546);
        accounting.register(LocalDate.parse("1983-01-14"),429.05,429.05,146.5622069);
        accounting.register(LocalDate.parse("1983-01-17"),429.4,429.4,146.4137644);
        accounting.register(LocalDate.parse("1983-01-18"),429.3,429.3,146.4137644);
        accounting.register(LocalDate.parse("1983-01-19"),427.2,427.2,146.0685652);
        accounting.register(LocalDate.parse("1983-01-20"),428.6,428.6,146.1670273);
        accounting.register(LocalDate.parse("1983-01-21"),424.85,424.85,148.2658359);
        accounting.register(LocalDate.parse("1983-01-24"),422.6,422.6,146.8599966);
        accounting.register(LocalDate.parse("1983-01-25"),423.15,423.15,150.6871335);
        accounting.register(LocalDate.parse("1983-01-26"),423.1,423.1,153.0806813);
        accounting.register(LocalDate.parse("1983-01-27"),427.25,427.25,152.4885971);
        accounting.register(LocalDate.parse("1983-01-28"),426.3,426.3,152.5422336);
        accounting.register(LocalDate.parse("1983-01-31"),428.15,428.15,148.2151641);
        accounting.register(LocalDate.parse("1983-02-01"),424.2,424.2,149.1838906);
        accounting.register(LocalDate.parse("1983-02-02"),425.0,425.0,147.3089572);
        accounting.register(LocalDate.parse("1983-02-03"),425.85,425.85,151.3708796);
        accounting.register(LocalDate.parse("1983-02-04"),429.35,429.35,150.5302222);
        accounting.register(LocalDate.parse("1983-02-07"),429.5,429.5,149.6471946);
        accounting.register(LocalDate.parse("1983-02-08"),427.1,427.1,146.1177797);
        accounting.register(LocalDate.parse("1983-02-09"),427.1,427.1,145.9702356);
        accounting.register(LocalDate.parse("1983-02-10"),430.95,430.95,148.3672836);
        accounting.register(LocalDate.parse("1983-02-11"),429.75,429.75,148.3672836);
        accounting.register(LocalDate.parse("1983-02-14"),431.9,431.9,144.55919);
        accounting.register(LocalDate.parse("1983-02-15"),430.3,430.3,145.72499);
        accounting.register(LocalDate.parse("1983-02-16"),429.2,429.2,143.6494104);
        accounting.register(LocalDate.parse("1983-02-17"),430.0,430.0,145.1883395);
        accounting.register(LocalDate.parse("1983-02-18"),430.2,430.2,146.2656223);
        accounting.register(LocalDate.parse("1983-02-22"),427.2,427.2,145.480567);
        accounting.register(LocalDate.parse("1983-02-23"),429.55,429.55,145.2856181);
        accounting.register(LocalDate.parse("1983-02-24"),432.65,432.65,145.2856181);
        accounting.register(LocalDate.parse("1983-02-25"),431.95,431.95,148.2658359);
        accounting.register(LocalDate.parse("1983-02-28"),430.15,430.15,145.9211205);
        accounting.register(LocalDate.parse("1983-03-01"),433.45,433.45,142.9392123);
        accounting.register(LocalDate.parse("1983-03-02"),433.5,433.5,143.6018444);
        accounting.register(LocalDate.parse("1983-03-03"),434.2,434.2,145.3343063);
        accounting.register(LocalDate.parse("1983-03-04"),434.6,434.6,142.1893672);
        accounting.register(LocalDate.parse("1983-03-07"),434.55,434.55,142.1427631);
        accounting.register(LocalDate.parse("1983-03-08"),432.05,432.05,141.4934976);
        accounting.register(LocalDate.parse("1983-03-09"),433.8,433.8,141.1251448);
        accounting.register(LocalDate.parse("1983-03-10"),431.9,431.9,141.171084);
        accounting.register(LocalDate.parse("1983-03-11"),432.2,432.2,143.5068068);
        accounting.register(LocalDate.parse("1983-03-14"),432.6,432.6,141.8637782);
        accounting.register(LocalDate.parse("1983-03-15"),432.8,432.8,143.6494104);
        accounting.register(LocalDate.parse("1983-03-16"),429.55,429.55,143.364486);
        accounting.register(LocalDate.parse("1983-03-17"),429.7,429.7,142.4228473);
        accounting.register(LocalDate.parse("1983-03-18"),430.7,430.7,142.236002);
        accounting.register(LocalDate.parse("1983-03-21"),432.65,432.65,145.3343063);
        accounting.register(LocalDate.parse("1983-03-22"),431.45,431.45,145.1883395);
        accounting.register(LocalDate.parse("1983-03-23"),435.05,435.05,144.2226705);
        accounting.register(LocalDate.parse("1983-03-24"),435.2,435.2,142.37609);
        accounting.register(LocalDate.parse("1983-03-25"),433.5,433.5,143.5068068);
        accounting.register(LocalDate.parse("1983-03-28"),433.5,433.5,140.1672819);
        accounting.register(LocalDate.parse("1983-03-29"),432.65,432.65,140.0315047);
        accounting.register(LocalDate.parse("1983-03-30"),434.9,434.9,141.5858864);
        accounting.register(LocalDate.parse("1983-03-31"),432.2,432.2,141.5858864);
        accounting.register(LocalDate.parse("1983-04-01"),Double.NaN, 432.2,142.37609);
        accounting.register(LocalDate.parse("1983-04-04"),433.55,433.55,140.3033226);
        accounting.register(LocalDate.parse("1983-04-05"),432.1,432.1,142.7980145);
        accounting.register(LocalDate.parse("1983-04-06"),431.7,431.7,142.7980145);
        accounting.register(LocalDate.parse("1983-04-07"),432.15,432.15,141.5396769);
        accounting.register(LocalDate.parse("1983-04-08"),432.2,432.2,142.8921154);
        accounting.register(LocalDate.parse("1983-04-11"),435.45,435.45,143.2697622);
        accounting.register(LocalDate.parse("1983-04-12"),435.95,435.95,142.8450494);
        accounting.register(LocalDate.parse("1983-04-13"),436.7,436.7,142.7980145);
        accounting.register(LocalDate.parse("1983-04-14"),438.75,438.75,139.8057931);
        accounting.register(LocalDate.parse("1983-04-15"),438.55,438.55,139.3565457);
        accounting.register(LocalDate.parse("1983-04-18"),439.4,439.4,138.6880621);
        accounting.register(LocalDate.parse("1983-04-19"),438.3,438.3,136.8931724);
        accounting.register(LocalDate.parse("1983-04-20"),440.55,440.55,137.0662358);
        accounting.register(LocalDate.parse("1983-04-21"),440.1,440.1,136.3337221);
        accounting.register(LocalDate.parse("1983-04-22"),440.15,440.15,137.2831814);
        accounting.register(LocalDate.parse("1983-04-25"),438.5,438.5,135.3550468);
        accounting.register(LocalDate.parse("1983-04-26"),442.5,442.5,135.7363287);
        accounting.register(LocalDate.parse("1983-04-27"),442.25,442.25,135.693858);
        accounting.register(LocalDate.parse("1983-04-28"),443.65,443.65,137.1095701);
        accounting.register(LocalDate.parse("1983-04-29"),444.6,444.6,133.7272803);
        accounting.register(LocalDate.parse("1983-05-02"),443.4,443.4,133.9337771);
        accounting.register(LocalDate.parse("1983-05-03"),443.55,443.55,132.7855389);
        accounting.register(LocalDate.parse("1983-05-04"),444.55,444.55,132.0175251);
        accounting.register(LocalDate.parse("1983-05-05"),445.2,445.2,132.9891353);
        accounting.register(LocalDate.parse("1983-05-06"),447.85,447.85,132.8669026);
        accounting.register(LocalDate.parse("1983-05-09"),447.45,447.45,132.0577254);
        accounting.register(LocalDate.parse("1983-05-10"),447.45,447.45,131.5370246);
        accounting.register(LocalDate.parse("1983-05-11"),446.35,446.35,129.4559911);
        accounting.register(LocalDate.parse("1983-05-12"),445.5,445.5,129.7658797);
        accounting.register(LocalDate.parse("1983-05-13"),446.2,446.2,129.7658797);
        accounting.register(LocalDate.parse("1983-05-16"),444.6,444.6,130.6257741);
        accounting.register(LocalDate.parse("1983-05-17"),444.9,444.9,131.2980836);
        accounting.register(LocalDate.parse("1983-05-18"),443.4,443.4,130.7439162);
        accounting.register(LocalDate.parse("1983-05-19"),442.5,442.5,132.0175251);
        accounting.register(LocalDate.parse("1983-05-20"),442.3,442.3,131.7768369);
        accounting.register(LocalDate.parse("1983-05-23"),444.3,444.3,132.9891353);
        accounting.register(LocalDate.parse("1983-05-24"),446.5,446.5,133.7272803);
        accounting.register(LocalDate.parse("1983-05-25"),447.25,447.25,133.8924267);
        accounting.register(LocalDate.parse("1983-05-26"),446.5,446.5,132.2590943);
        accounting.register(LocalDate.parse("1983-05-27"),445.95,445.95,130.5078453);
        accounting.register(LocalDate.parse("1983-05-30"),Double.NaN, 445.95,129.9213811);
        accounting.register(LocalDate.parse("1983-05-31"),442.55,442.55,130.5078453);
        accounting.register(LocalDate.parse("1983-06-01"),443.3,443.3,130.9412953);
        accounting.register(LocalDate.parse("1983-06-02"),444.8,444.8,130.9412953);
        accounting.register(LocalDate.parse("1983-06-03"),445.0,445.0,133.6860574);
        accounting.register(LocalDate.parse("1983-06-06"),445.8,445.8,133.0707487);
        accounting.register(LocalDate.parse("1983-06-07"),442.25,442.25,131.8569687);
        accounting.register(LocalDate.parse("1983-06-08"),441.3,441.3,131.6968023);
        accounting.register(LocalDate.parse("1983-06-09"),442.25,442.25,131.0600091);
        accounting.register(LocalDate.parse("1983-06-10"),442.85,442.85,133.9337771);
        accounting.register(LocalDate.parse("1983-06-13"),445.7,445.7,134.724315);
        accounting.register(LocalDate.parse("1983-06-14"),445.55,445.55,133.9337771);
        accounting.register(LocalDate.parse("1983-06-15"),448.55,448.55,133.4392523);

        this.statistics = accounting.getStatistics(100_000);
    }

    @Test
    void testAccountingReturn() {
        assertThat(statistics.averageDailyReturn()).isEqualTo(0.16653429597914524);
        assertThat(statistics.annualisedReturn()).isEqualTo(42.63277977066118);
    }

    @Test
    void testAccountingReturnStddev() {
        assertThat(statistics.dailyStandardDeviation()).isEqualTo(1.5812523305355073);
        assertThat(statistics.annualisedStandardDeviation()).isEqualTo(25.300037288568117);
    }

    @Test
    void testAccountingSharpeRatio() {
        assertThat(statistics.dailySharpeRatio()).isEqualTo(0.10531797662093986);
        assertThat(statistics.annualisedSharpeRatio()).isEqualTo(1.6850876259350378);
    }

    @Test
    void testAccountingSkew() {
        assertThat(statistics.skew()).isEqualTo(0.09108485735373112);
    }

    @Test
    void testAccountingDrawdown() {
        assertThat(statistics.averageDrawdown()).isEqualTo(2.355175523309845);
        assertThat(statistics.maxDrawdown()).isEqualTo(10.538755342025);
    }

    @Test
    void testTails() {
        assertThat(statistics.upperTail()).isEqualTo(1.7618635902910709);
        assertThat(statistics.lowerTail()).isEqualTo(1.151050692293032);
    }

    @Test
    void testTurnover() {
        assertThat(statistics.turnover()).isEqualTo(2.775006384612964);
    }
}
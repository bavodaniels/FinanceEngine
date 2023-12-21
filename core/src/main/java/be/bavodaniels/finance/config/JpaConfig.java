package be.bavodaniels.finance.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"be.bavodaniels"})
@EnableJpaRepositories(basePackages = {"be.bavodaniels"})
public class JpaConfig {
}

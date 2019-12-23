package de.daikol.motivator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This class is the main application.
 */
@EnableJpaRepositories("de.daikol.motivator.repository")
@EntityScan("de.daikol.motivator.model")
@EnableTransactionManagement
@SpringBootApplication
@PropertySource("file:/home/motivator/security.properties")
public class MotivatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotivatorApplication.class, args);
    }


}

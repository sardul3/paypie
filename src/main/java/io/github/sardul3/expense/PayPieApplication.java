package io.github.sardul3.expense;

import io.github.sardul3.expense.config.AppApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppApiProperties.class)
public class PayPieApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayPieApplication.class, args);
    }
}

package com.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
// @EnableOAuth2Sso
public class StockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockServiceApplication.class, args);
    }

    @Bean
    public AuditorAware<String> bean_name() {
        return () -> Optional.of("testuser"); // XXX Hook this up to oauth2 connected user
    }

}

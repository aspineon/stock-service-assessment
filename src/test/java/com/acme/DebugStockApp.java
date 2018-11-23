package com.acme;

import com.acme.StockServiceApplication;
import org.springframework.boot.SpringApplication;

public class DebugStockApp {
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(StockServiceApplication.class);
        app.setAdditionalProfiles("test");
        app.run(args);
    }
}

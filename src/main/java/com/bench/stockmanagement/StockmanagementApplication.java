package com.bench.stockmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class StockmanagementApplication {

    private static RateExchanger rateExchanger;
    private static RateReader rateReader;

    @Autowired
    public StockmanagementApplication(RateExchanger rateExchanger, RateReader rateReader) {
        this.rateExchanger = rateExchanger;
        this.rateReader = rateReader;
    }

    public static void main(String[] args) {
        SpringApplication.run(StockmanagementApplication.class, args);
        Double rateFor = rateExchanger.getRateFor(LocalDate.parse("2022-02-27"));
        System.out.println(rateFor);
    }

}

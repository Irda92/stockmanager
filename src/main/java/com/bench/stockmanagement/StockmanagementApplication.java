package com.bench.stockmanagement;

import com.bench.stockmanagement.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class StockmanagementApplication {

    private static RateExchanger rateExchanger;
    private static Reader reader;

    @Autowired
    public StockmanagementApplication(RateExchanger rateExchanger, Reader reader) {
        StockmanagementApplication.rateExchanger = rateExchanger;
        StockmanagementApplication.reader = reader;
    }

    public static void main(String[] args) {
        SpringApplication.run(StockmanagementApplication.class, args);
        Double rateFor = rateExchanger.getRateFor(LocalDate.parse("2022-02-27"));
        System.out.println(rateFor);


        List<Order> orders = reader.readOrder();
        orders.forEach(System.out::println);
    }

}

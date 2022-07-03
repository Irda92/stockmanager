package com.bench.stockmanagement;

import com.bench.stockmanagement.domain.Order;
import com.bench.stockmanagement.domain.UsdRate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class Reader {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd.");

    public List<UsdRate> readRate() {
        Path path = Paths.get("src/main/resources/usd.csv");
        List<UsdRate> usdRates = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] lineParts = line.split(",");
                LocalDate localDate = LocalDate.parse(lineParts[0], FORMATTER);
                double rate = Double.parseDouble(lineParts[1]);
                usdRates.add(new UsdRate(localDate, rate));
            }
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return usdRates;
    }

    public List<Order> readOrder() {
        Path path = Paths.get("src/main/resources/order.csv");
        List<Order> orders = new ArrayList<>();
        try {
            System.out.println("Read orders...");
            List<String> lines = Files.readAllLines(path);
            System.out.println("Lines... " + lines);
            LocalDate orderDate = LocalDate.parse(lines.get(0), FORMATTER);
            Double shippingCost = Double.parseDouble(lines.get(1));
            for (String line : lines.subList(2, lines.size())) {
                System.out.println("Order... " + line);
                String[] lineParts = line.split(",");
                String item = lineParts[0];
                String name = lineParts[1];
                Integer quantity = Integer.parseInt(lineParts[2]);
                double price = Double.parseDouble(lineParts[3]);
                double totalPrice = Double.parseDouble(lineParts[4]);
                orders.add(Order.builder().date(orderDate).shippingCost(shippingCost).itemNumber(item).englishName(name)
                                   .quantity(quantity).price(price).totalPrice(totalPrice)
                                   .build());
            }
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return orders;
    }
}

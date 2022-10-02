package com.bench.stockmanagement.services;

import com.bench.stockmanagement.domain.Order;
import com.bench.stockmanagement.domain.OrderedProduct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderReader {
    private static final String ORDERS_PATH = "src/main/resources/orders";

    public List<Order> readOrder() {
        Path path = Paths.get(ORDERS_PATH);
        try {
            return Files.list(path)
                    .map(this::readOrder)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return Collections.emptyList();
    }

    public Order readOrder(Path path) {
        System.out.println(path);
        try {
            List<String> lines = Files.readAllLines(path);

            String firstLine = lines.get(0);
            String[] firstLineParts = firstLine.split(",");
            String seller = firstLineParts[0].substring(1);
            LocalDate date = LocalDate.parse(firstLineParts[1]);
            double totalShippingCost = Double.parseDouble(firstLineParts[2]);
            int totalCount = Integer.parseInt(firstLineParts[3]);
            String currency = firstLineParts[4];

            List<String> orderLines = lines.subList(1, lines.size());
            List<OrderedProduct> orderedProducts = orderLines.stream()
                                                             .map(this::createOrderedProduct)
                                                             .collect(Collectors.toList());

            return new Order(seller, date, totalShippingCost, totalCount, currency, orderedProducts);
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return null;
    }

    private OrderedProduct createOrderedProduct(String line) {
        System.out.println(line);
        String[] lineParts = line.split(",");
        String item = lineParts[0];
        String englishName = lineParts[1];
        String hungarianName = lineParts[2];
        String attribute = lineParts[3];
        Integer quantity = Integer.parseInt(lineParts[4]);
        double price = Double.parseDouble(lineParts[5]);

        return new OrderedProduct(item, englishName, hungarianName, attribute, quantity, price);
    }
}

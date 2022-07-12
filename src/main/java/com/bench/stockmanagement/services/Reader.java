package com.bench.stockmanagement.services;

import com.bench.stockmanagement.dataaccess.Order;
import com.bench.stockmanagement.dataaccess.OrderedProduct;
import com.bench.stockmanagement.dataaccess.SoldItem;
import com.bench.stockmanagement.domain.UsdRate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Reader {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
    public static final String RESOURCE_PATH = "src/main/resources/";

    public List<UsdRate> readRate() {
        Path path = Paths.get(RESOURCE_PATH + "usd.csv");
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

    public Order readOrder(String fileName) {
        Path path = Paths.get(RESOURCE_PATH + fileName);
        try {
            List<String> lines = Files.readAllLines(path);
            String orderDate = lines.get(0);
            String shippingDetailsLine = lines.get(1);
            String[] shippingDetailsLineParts = shippingDetailsLine.split(",");
            Double shippingCost = Double.parseDouble(shippingDetailsLineParts[0]);
            Integer totalCount = Integer.parseInt(shippingDetailsLineParts[1]);

            List<String> orderLines = lines.subList(2, lines.size());
            List<OrderedProduct> orderedProducts = orderLines.stream()
                                                             .map(this::createOrderedProduct)
                                                             .collect(Collectors.toList());
            return new Order("test-order", orderDate, shippingCost, totalCount, orderedProducts);
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return null;
    }

    public List<SoldItem> readSoldItems(String fileName) {
        Path path = Paths.get(RESOURCE_PATH + fileName);
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.ISO_8859_1);

            return lines.stream()
                        .map(this::createSoldProducts)
                        .collect(Collectors.toList());
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return Collections.emptyList();
    }

    private SoldItem createSoldProducts(String line) {
        String[] lineParts = line.split(",");

        String receiptNumber = lineParts[0];
        String hungarianName = lineParts[1];
        String itemNumber = lineParts[2];
        Integer quantity = lineParts[3].isEmpty() ? 0 :Integer.parseInt(lineParts[3]);
        Integer price = Integer.parseInt(lineParts[4]);
        String date = lineParts[5];

        return new SoldItem(receiptNumber, date, itemNumber, hungarianName, price, quantity);
    }

    private OrderedProduct createOrderedProduct(String line) {
        String[] lineParts = line.split(",");
        String item = lineParts[0];
        String name = lineParts[1];
        Integer quantity = Integer.parseInt(lineParts[2]);
        double price = Double.parseDouble(lineParts[3]);

        return new OrderedProduct(item, name, quantity, price);
    }
}

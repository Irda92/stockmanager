package com.bench.stockmanagement.services;

import com.bench.stockmanagement.dataaccess.Order;
import com.bench.stockmanagement.dataaccess.OrderedProduct;
import com.bench.stockmanagement.dataaccess.Receipt;
import com.bench.stockmanagement.dataaccess.SoldItem;
import com.bench.stockmanagement.domain.UsdRate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.bench.stockmanagement.Constants.FORMATTER_WITH_DOTS;

@Component
public class Reader {

    public static final String ORDERS_PATH = "src/main/resources/orders";
    public static final String SELLING_PATH = "src/main/resources/selling";
    public static final String RATE_PATH = "src/main/resources/usd.csv";

    public List<UsdRate> readRate() {
        Path path = Paths.get(RATE_PATH);
        List<UsdRate> usdRates = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] lineParts = line.split(",");
                LocalDate localDate = LocalDate.parse(lineParts[0], FORMATTER_WITH_DOTS);
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

    public List<Receipt> readSoldItems() {
        Path path = Paths.get(SELLING_PATH);
        try {
            return Files.list(path)
                        .map(this::readSoldItems)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return Collections.emptyList();
    }

    private Order readOrder(Path path) {
        try {
            List<String> lines = Files.readAllLines(path);

            //There is a zero width no-break space character at the beginning
            //TODO other way to remove it
            String orderDate = lines.get(0).substring(1);

            String shippingDetailsLine = lines.get(1);
            String[] shippingDetailsLineParts = shippingDetailsLine.split(",");
            Double shippingCost = Double.parseDouble(shippingDetailsLineParts[0]);
            Integer totalCount = Integer.parseInt(shippingDetailsLineParts[1]);
            String currency = shippingDetailsLineParts[2];

            List<String> orderLines = lines.subList(2, lines.size());
            List<OrderedProduct> orderedProducts = orderLines.stream()
                                                             .map(this::createOrderedProduct)
                                                             .collect(Collectors.toList());
            String id = path.getFileName().toString();

            return new Order(id, orderDate, shippingCost, totalCount, currency, orderedProducts);
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return null;
    }

    private OrderedProduct createOrderedProduct(String line) {
        String[] lineParts = line.split(",");
        String item = lineParts[0];
        String englishName = lineParts[1];
        String hungarianName = lineParts[2];
        Integer quantity = Integer.parseInt(lineParts[3]);
        double price = Double.parseDouble(lineParts[4]);

        return new OrderedProduct(item, englishName, hungarianName, quantity, price);
    }

    private List<Receipt> readSoldItems(Path path) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            List<SoldItem> soldItems;
            List<Receipt> receiptList = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                soldItems = new ArrayList<>();
                String[] lineParts = lines.get(i).split(",");
                String receiptNumber = lineParts[0];
                String date = lineParts[5];

                soldItems.add(createSoldItem(lines.get(i)));

                String nextLineReceiptNumber;
                do {
                    String[] nextLineParts = lines.get(i + 1)
                                                  .split(",");
                    nextLineReceiptNumber = nextLineParts[0];

                    if (receiptNumber.equals(nextLineReceiptNumber)) {
                        soldItems.add(createSoldItem(lines.get(i + 1)));
                        i++;
                    }
                } while (receiptNumber.equals(nextLineReceiptNumber) && i < lines.size() - 1);

                receiptList.add(new Receipt(receiptNumber, date, soldItems));
            }


            return receiptList;
        } catch (IOException e) {
            //TODO add logger
            System.out.println("Something happened: " + e);
        }
        return Collections.emptyList();
    }

    private SoldItem createSoldItem(String line) {
        String[] lineParts = line.split(",");

        String hungarianName = lineParts[1];
        String itemNumber = lineParts[2];
        Integer quantity = lineParts[3].isEmpty() ? 0 : Integer.parseInt(lineParts[3]);
        Integer price = Integer.parseInt(lineParts[4]);

        return new SoldItem(itemNumber, hungarianName, price, quantity);
    }
}

package com.bench.stockmanagement.services;

import com.bench.stockmanagement.domain.Receipt;
import com.bench.stockmanagement.domain.SoldItem;
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
public class SellingReader {
    private static final String SELLING_PATH = "src/main/resources/selling";
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

    private List<Receipt> readSoldItems(Path path) {
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            List<SoldItem> soldItems;
            List<Receipt> receiptList = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                soldItems = new ArrayList<>();
                String[] lineParts = lines.get(i).split(",");
                String receiptNumber = lineParts[0];
                LocalDate date = LocalDate.parse(lineParts[5], FORMATTER_WITH_DOTS);

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
        String attribute = null;
        Integer quantity = lineParts[3].isEmpty() ? 1 : Integer.parseInt(lineParts[3]);
        Integer price = Integer.parseInt(lineParts[4]);

        return new SoldItem(itemNumber, hungarianName, attribute, price, quantity);
    }
}

package com.bench.stockmanagement.mappers;

import com.bench.stockmanagement.UnexpectedQueryResultException;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.Receipt;
import com.bench.stockmanagement.domain.SoldItem;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SellingMapper {

    public List<DBReceipt> mapToDB(List<Receipt> receipts) {
        return receipts.stream().map(this::map).flatMap(List::stream).collect(Collectors.toList());
    }
    public List<DBReceipt> map(Receipt receipt) {
        String receiptNumber = receipt.getReceiptNumber();
        String date = receipt.getDate().toString();

        List<DBReceipt> receipts = new ArrayList<>();

        for (SoldItem product : receipt.getItems()) {
            String itemNumber = product.getItemNumber();
            String hungarianName = product.getHungarianName();
            String attribute = product.getAttribute();
            Integer price = product.getPrice();
            Integer quantity = product.getQuantity();
            String id = receiptNumber + "_" + itemNumber;

            DBReceipt dbReceipt = new DBReceipt(id, receiptNumber, date, itemNumber, hungarianName, attribute, price,
                    quantity);
            receipts.add(dbReceipt);
        }
        return receipts;
    }

    public List<Receipt> mapReceipts(List<DBReceipt> receiptResults) {
        if(receiptResults.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, List<DBReceipt>> receiptMap = new HashMap<>();

        List<String> receiptNumbers = receiptResults.stream()
                .map(DBReceipt::getReceiptNumber)
                .distinct()
                .collect(Collectors.toList());

        for (String receiptNumber : receiptNumbers) {
            List<DBReceipt> list = receiptResults.stream()
                    .filter(result -> result.getReceiptNumber().equals(receiptNumber))
                    .collect(Collectors.toList());
            receiptMap.put(receiptNumber, list);
        }

        return receiptMap.values().stream().map(this::mapReceipt).collect(Collectors.toList());
    }

    public Receipt mapReceipt(List<DBReceipt> receiptResults) {
        if (receiptResults.isEmpty()) {
            return null;
        }
        String receiptNumber = getReceiptNumber(receiptResults);
        LocalDate date = getDate(receiptResults);
        List<SoldItem> items = getItems(receiptResults);
        return Receipt.builder().receiptNumber(receiptNumber).date(date).items(items).build();
    }

    public List<SoldItem> getItems(List<DBReceipt> receiptResults) {
        return receiptResults.stream()
                .map(result -> new SoldItem(result.getItemNumber(), result.getHungarianName(), result.getAttribute(),
                        result.getPrice(), result.getQuantity()))
                .collect(
                        Collectors.toList());
    }

    private String getReceiptNumber(List<DBReceipt> receiptResults) {
        List<String> receiptNumbers = receiptResults.stream()
                .map(DBReceipt::getReceiptNumber)
                .distinct()
                .collect(Collectors.toList());
        if (receiptNumbers.size() == 1) {
            return receiptNumbers.get(0);
        } else {
            throw new UnexpectedQueryResultException("Unexpected receipt number returned! Result=" + receiptNumbers);
        }
    }

    private LocalDate getDate(List<DBReceipt> receiptResults) {
        List<String> dates = receiptResults.stream().map(DBReceipt::getDate).distinct().collect(Collectors.toList());
        if (dates.size() == 1) {
            return LocalDate.parse(dates.get(0));
        } else {
            throw new UnexpectedQueryResultException("Unexpected dates returned! Result=" + dates);
        }
    }
}

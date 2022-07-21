package com.bench.stockmanagement.mappers;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.*;
import com.bench.stockmanagement.services.RateExchanger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class ProductMapper {
    private final RateExchanger rateExchanger;

    public ProductMapper(RateExchanger rateExchanger) {
        this.rateExchanger = rateExchanger;
    }

    public Product map(String itemNumber, List<DBOrder> orderedProducts, List<DBReceipt> soldItems) {
        String englishName = getEnglishName(orderedProducts);
        String hungarianName = getHungarianName(orderedProducts, soldItems);
        List<PurchaseInformation> purchaseInformationList = getPurchaseInformation(orderedProducts);
        List<SellingInformation> sellingInformationList = getSellingInformation(soldItems);
        int quantity = calculateQuantity(purchaseInformationList, sellingInformationList);

        int actualProfit = calculateActualProfit(purchaseInformationList, sellingInformationList);

        return new Product(itemNumber, englishName, hungarianName, quantity, sellingInformationList,
                purchaseInformationList, actualProfit);
    }

    private int calculateActualProfit(List<PurchaseInformation> purchaseInformationList, List<SellingInformation> sellingInformationList) {
        purchaseInformationList.sort(Comparator.comparing(PurchaseInformation::getOrderDate).reversed());
        sellingInformationList.sort(Comparator.comparing(SellingInformation::getSellingDate).reversed());

        int actualSellingPrice = sellingInformationList.stream()
                .findFirst()
                .map(SellingInformation::getPrice)
                .orElse(0);
        int actualPurchasedPrice = purchaseInformationList.stream()
                .findFirst()
                .map(PurchaseInformation::getPurchasePrice)
                .orElse(0);
        return actualSellingPrice - actualPurchasedPrice;
    }

    private String getHungarianName(List<DBOrder> orderedProducts, List<DBReceipt> soldItems) {
        String hungarianName = orderedProducts.stream()
                .map(DBOrder::getHungarianName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        return soldItems.stream().map(DBReceipt::getHungarianName).findFirst().orElse(hungarianName);
    }

    private String getEnglishName(List<DBOrder> orderedProducts) {
        return orderedProducts.stream()
                .map(DBOrder::getEnglishName)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private Integer calculateQuantity(List<PurchaseInformation> purchaseInformationList, List<SellingInformation> sellingInformationList) {
        int purchasedQuantity = purchaseInformationList.stream().map(PurchaseInformation::getQuantity).reduce(
                Integer::sum).orElse(0);
        int soldQuantity = sellingInformationList.stream()
                .map(SellingInformation::getQuantity)
                .reduce(Integer::sum)
                .orElse(0);
        return purchasedQuantity - soldQuantity;
    }

    private List<SellingInformation> getSellingInformation(List<DBReceipt> soldItems) {
        List<SellingInformation> sellingInformationList = new ArrayList<>();

        for (DBReceipt receipt : soldItems) {
            LocalDate orderDate = LocalDate.parse(receipt.getDate());
            int quantity = receipt.getQuantity();
            int price = receipt.getPrice();
            String attribute = receipt.getAttribute();

            SellingInformation sellingInformation = new SellingInformation(orderDate, quantity, price, attribute);

            sellingInformationList.add(sellingInformation);
        }
        return sellingInformationList;
    }

    private List<PurchaseInformation> getPurchaseInformation(List<DBOrder> orderedProducts) {
        List<PurchaseInformation> purchaseInformationList = new ArrayList<>();

        for (DBOrder order : orderedProducts) {
            LocalDate orderDate = LocalDate.parse(order.getDate());
            String currency = order.getCurrency();
            double rate = getRate(currency, orderDate);
            int quantity = order.getQuantity();
            int productPrice = (int) Math.round(order.getPrice() * rate);
            int shippingCost = (int) Math.round(order.getShippingCost() * rate);
            int purchasePrice = productPrice + shippingCost;
            String attribute = order.getAttribute();

            PurchaseInformation purchaseInformation = new PurchaseInformation(orderDate, quantity, productPrice,
                    shippingCost, purchasePrice, attribute);

            purchaseInformationList.add(purchaseInformation);
        }
        return purchaseInformationList;
    }

    private double getRate(String currency, LocalDate orderDate) {
        if ("HUF".equals(currency)) {
            return 1;
        }
        return rateExchanger.getRateFor(orderDate);
    }
}

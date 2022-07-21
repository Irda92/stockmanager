package com.bench.stockmanagement.domain;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class OrderedProduct {
    private final String itemNumber;
    private final String englishName;
    private final String hungarianName;
    private final String attribute;
    private final Integer quantity;
    private final Double price;
    private final Double totalPrice;

    public OrderedProduct(String itemNumber, String englishName, String hungarianName, String attribute, Integer quantity, Double price) {
        this.itemNumber = itemNumber;
        this.englishName = englishName;
        this.hungarianName = hungarianName;
        this.attribute = attribute;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = quantity * price;
    }

}

package com.bench.stockmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
public class PurchaseInformation {
    public static final PurchaseInformation ZERO = new PurchaseInformation(null, 0, 0, 0, 0, null);

    private LocalDate orderDate;
    private int quantity;
    private int productPrice;
    private int shippingCost;
    private int purchasePrice;
    private String attribute;
}

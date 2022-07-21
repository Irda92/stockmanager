package com.bench.stockmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class Product {
    private String itemNumber;
    private String englishName;
    private String hungarianName;
    private int quantity;
    private List<SellingInformation> sellingInformation;
    private List<PurchaseInformation> purchaseInformation;
    private int actualProfit;
}

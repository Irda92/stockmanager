package com.bench.stockmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class ProductStockData {
    private String itemNumber;
    private String attribute;
    private Integer actualPrice;
    private Integer minStock;
    private Integer maxStock;
    private Double lastPurchaseCost;
    private LocalDate lastPurchaseDate;
}

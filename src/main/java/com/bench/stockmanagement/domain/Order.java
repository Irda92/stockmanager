package com.bench.stockmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class Order {
    private final String itemNumber;
    private final String englishName;
    private final Integer quantity;
    private final double price;
    private final double totalPrice;
    private final double shippingCost;
    private final LocalDate date;
}

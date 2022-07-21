package com.bench.stockmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class Order {
    private String seller;
    private LocalDate date;
    private Double totalShippingCost;
    private Integer totalProductCount;
    private String currency;
    private List<OrderedProduct> products;
}

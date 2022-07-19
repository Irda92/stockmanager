package com.bench.stockmanagement.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Product {
    private final String itemNumber;
    private final String englishName;
    private final String hungarianName;
    private final Integer priceToSell;
    private final Integer priceToBuy;
    private final Integer quantity;
    private final Integer ordered;
    private final Integer sold;
}

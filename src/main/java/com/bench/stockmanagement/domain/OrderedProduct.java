package com.bench.stockmanagement.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderedProduct {
    private final String itemNumber;
    private final String englishName;
    private final Double priceToBuy;
    private final Integer ordered;
}

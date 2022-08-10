package com.bench.stockmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class SoldItem {
    private String itemNumber;
    private String hungarianName;
    private String attribute;
    private Integer price;
    private Integer quantity;
}

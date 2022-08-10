package com.bench.stockmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
public class SellingInformation {
    private LocalDate sellingDate;
    private int quantity;
    private int price;
    private String attribute;
}

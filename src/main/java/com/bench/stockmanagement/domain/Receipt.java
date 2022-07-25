package com.bench.stockmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
public class Receipt {
    public static final Receipt EMPTY = new Receipt("", null, Collections.emptyList());

    private String receiptNumber;
    private LocalDate date;
    private List<SoldItem> items;
}

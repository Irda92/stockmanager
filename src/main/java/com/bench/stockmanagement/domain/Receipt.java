package com.bench.stockmanagement.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
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
public class Receipt {
    private String receiptNumber;
    private LocalDate date;
    private List<SoldItem> items;
}

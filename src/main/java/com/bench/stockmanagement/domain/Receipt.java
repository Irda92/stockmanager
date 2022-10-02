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
public class Receipt {
    public static final Receipt EMPTY = new Receipt("", null, Collections.emptyList());

    private String receiptNumber;
    private LocalDate date;
    private List<SoldItem> items;

    public Receipt(String receiptNumber, LocalDate date, List<SoldItem> items)
    {
        this.receiptNumber = receiptNumber;
        this.date = date;
        this.items = items;
    }

    public String getReceiptNumber()
    {
        return receiptNumber;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public List<SoldItem> getItems()
    {
        return items;
    }
}

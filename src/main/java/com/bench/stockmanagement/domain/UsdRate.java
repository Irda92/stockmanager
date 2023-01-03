package com.bench.stockmanagement.domain;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
public class UsdRate {
    private final LocalDate date;
    private final double rate;

    public UsdRate(LocalDate date, double rate) {
        this.date = date;
        this.rate = rate;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public double getRate()
    {
        return rate;
    }
}

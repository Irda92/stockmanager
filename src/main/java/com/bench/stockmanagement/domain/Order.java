package com.bench.stockmanagement.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    public static final Order EMPTY = new Order();

    private String seller;
    private LocalDate date;
    private Double totalShippingCost;
    private Integer totalProductCount;
    private String currency;
    private List<OrderedProduct> products;

    public String getSeller()
    {
        return seller;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public Double getTotalShippingCost()
    {
        return totalShippingCost;
    }

    public Integer getTotalProductCount()
    {
        return totalProductCount;
    }

    public String getCurrency()
    {
        return currency;
    }

    public List<OrderedProduct> getProducts()
    {
        return products;
    }
}

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

//    public SoldItem(String itemNumber, String hungarianName, String attribute, Integer price, Integer quantity)
//    {
//        this.itemNumber = itemNumber;
//        this.hungarianName = hungarianName;
//        this.attribute = attribute;
//        this.price = price;
//        this.quantity = quantity;
//    }

    public String getItemNumber()
    {
        return itemNumber;
    }

    public String getHungarianName()
    {
        return hungarianName;
    }

    public String getAttribute()
    {
        return attribute;
    }

    public Integer getPrice()
    {
        return price;
    }

    public Integer getQuantity()
    {
        return quantity;
    }
}

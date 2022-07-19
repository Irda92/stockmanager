package com.bench.stockmanagement.dataaccess;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class OrderedProduct {
    private String itemNumber;
    private String englishName;
    private String hungarianName;
    private Integer quantity;
    private Double price;
    private Double totalPrice;

    public OrderedProduct() {
    }

    public OrderedProduct(String itemNumber, String englishName, String hungarianName, Integer quantity, Double price) {
        this.itemNumber = itemNumber;
        this.englishName = englishName;
        this.hungarianName = hungarianName;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = quantity * price;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getHungarianName() {
        return hungarianName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void setHungarianName(String hungarianName) {
        this.hungarianName = hungarianName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

package com.bench.stockmanagement.dataaccess;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class DBOrder {
    private String id;
    private String seller;
    private String date;
    private Double shippingCost;
    private String currency;
    private String itemNumber;
    private String englishName;
    private String hungarianName;
    private String attribute;
    private Integer quantity;
    private Double price;

    public DBOrder() {
    }

    public DBOrder(String id, String seller, String date, Double shippingCost, String currency, String itemNumber,
            String englishName, String hungarianName, String attribute, Integer quantity, Double price) {
        this.id = id;
        this.seller = seller;
        this.date = date;
        this.shippingCost = shippingCost;
        this.currency = currency;
        this.itemNumber = itemNumber;
        this.englishName = englishName;
        this.hungarianName = hungarianName;
        this.attribute = attribute;
        this.quantity = quantity;
        this.price = price;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "order_index")
    public String getSeller() {
        return seller;
    }

    @DynamoDbSecondarySortKey(indexNames = "order_index")
    @DynamoDbAttribute("orderDate")
    public String getDate() {
        return date;
    }

    public Double getShippingCost() {
        return shippingCost;
    }

    public String getCurrency() {
        return currency;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "product_index")
    public String getItemNumber() {
        return itemNumber;
    }

    public String getEnglishName() {
        return englishName;
    }

    public String getHungarianName() {
        return hungarianName;
    }

    public String getAttribute() {
        return attribute;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

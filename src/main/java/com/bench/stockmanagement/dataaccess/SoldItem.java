package com.bench.stockmanagement.dataaccess;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import lombok.ToString;

@ToString
@DynamoDBDocument
public class SoldItem {
    private String itemNumber;
    private String hungarianName;
    private Integer price;
    private Integer quantity;

    public SoldItem() {
    }

    public SoldItem(String itemNumber, String hungarianName, Integer price, Integer quantity) {
        this.itemNumber = itemNumber;
        this.hungarianName = hungarianName;
        this.price = price;
        this.quantity = quantity;
    }

    @DynamoDBAttribute
    public String getItemNumber() {
        return itemNumber;
    }

    @DynamoDBAttribute
    public String getHungarianName() {
        return hungarianName;
    }

    @DynamoDBAttribute
    public Integer getPrice() {
        return price;
    }

    @DynamoDBAttribute
    public Integer getQuantity() {
        return quantity;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setHungarianName(String hungarianName) {
        this.hungarianName = hungarianName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

package com.bench.stockmanagement.dataaccess;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "SoldItems")
public class SoldItem {
    private String id;
    private Integer receiptNumber;
    private String date;
    private String itemNumber;
    private String hungarianName;
    private Integer price;
    private Integer quantity;

    public SoldItem(Integer receiptNumber, String date, String itemNumber, String hungarianName, Integer price,
                    Integer quantity) {
        this.id = receiptNumber + "-" + itemNumber;
        this.receiptNumber = receiptNumber;
        this.date = date;
        this.itemNumber = itemNumber;
        this.hungarianName = hungarianName;
        this.price = price;
        this.quantity = quantity;
    }

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    public Integer getReceiptNumber() {
        return receiptNumber;
    }

    public String getDate() {
        return date;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public String getHungarianName() {
        return hungarianName;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setReceiptNumber(Integer receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public void setDate(String date) {
        this.date = date;
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

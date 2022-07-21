package com.bench.stockmanagement.dataaccess;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Selling")
public class DBReceipt {
    private String id;
    private String receiptNumber;
    private String date;
    private String itemNumber;
    private String hungarianName;
    private String attribute;
    private Integer price;
    private Integer quantity;

    public DBReceipt() {
    }

    public DBReceipt(String id, String receiptNumber, String date, String itemNumber, String hungarianName, String attribute, Integer price, Integer quantity) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        this.date = date;
        this.itemNumber = itemNumber;
        this.hungarianName = hungarianName;
        this.attribute = attribute;
        this.price = price;
        this.quantity = quantity;
    }

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "receipt_index")
    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    @DynamoDBIndexRangeKey(attributeName = "sellingDate", globalSecondaryIndexName = "receipt_index")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "sold_item_index")
    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getHungarianName() {
        return hungarianName;
    }

    public void setHungarianName(String hungarianName) {
        this.hungarianName = hungarianName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}

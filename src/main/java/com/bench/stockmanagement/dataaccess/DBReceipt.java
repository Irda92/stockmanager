package com.bench.stockmanagement.dataaccess;

import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@ToString
@DynamoDbBean
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

    public DBReceipt(String id, String receiptNumber, String date, String itemNumber, String hungarianName,
            String attribute, Integer price, Integer quantity) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        this.date = date;
        this.itemNumber = itemNumber;
        this.hungarianName = hungarianName;
        this.attribute = attribute;
        this.price = price;
        this.quantity = quantity;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "receipt_index")
    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    @DynamoDbSecondarySortKey(indexNames = "sold_item_index")
    @DynamoDbAttribute("sellingDate")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "sold_item_index")
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

package com.bench.stockmanagement.dataaccess;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.ToString;

import java.util.List;

@ToString
@DynamoDBTable(tableName = "SoldItems")
public class Receipt {
    private String receiptNumber;
    private String date;
    private List<SoldItem> items;

    public Receipt() {
    }

    public Receipt(String receiptNumber, String date, List<SoldItem> items) {
        this.receiptNumber = receiptNumber;
        this.date = date;
        this.items = items;
    }

    @DynamoDBHashKey
    @DynamoDBAttribute
    public String getReceiptNumber() {
        return receiptNumber;
    }

    @DynamoDBRangeKey
    @DynamoDBAttribute(attributeName = "sellingDate")
    public String getDate() {
        return date;
    }

    @DynamoDBAttribute
    public List<SoldItem> getItems() {
        return items;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setItems(List<SoldItem> items) {
        this.items = items;
    }
}

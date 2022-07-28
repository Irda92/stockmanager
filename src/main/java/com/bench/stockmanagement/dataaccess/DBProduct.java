package com.bench.stockmanagement.dataaccess;

import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;

@ToString
@DynamoDbBean
public class DBProduct {
    private String itemNumber;
    private Integer lastSellingPrice;
    private Map<String, Integer> minStock;
    private Map<String, Integer> maxStock;
    private Map<String, Double> costs;

    public DBProduct() {
    }

    public DBProduct(String itemNumber, Integer lastSellingPrice, Map<String, Integer> minStock,
            Map<String, Integer> maxStock, Map<String, Double> costs) {
        this.itemNumber = itemNumber;
        this.lastSellingPrice = lastSellingPrice;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.costs = costs;
    }

    @DynamoDbPartitionKey
    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getLastSellingPrice() {
        return lastSellingPrice;
    }

    public void setLastSellingPrice(Integer lastSellingPrice) {
        this.lastSellingPrice = lastSellingPrice;
    }

    public Map<String, Integer> getMinStock() {
        return minStock;
    }

    public void setMinStock(Map<String, Integer> minStock) {
        this.minStock = minStock;
    }

    public Map<String, Integer> getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Map<String, Integer> maxStock) {
        this.maxStock = maxStock;
    }

    public Map<String, Double> getCosts() {
        return costs;
    }

    public void setCosts(Map<String, Double> costs) {
        this.costs = costs;
    }

}

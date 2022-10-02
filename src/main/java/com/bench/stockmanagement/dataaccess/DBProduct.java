package com.bench.stockmanagement.dataaccess;

import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;

@ToString
@DynamoDbBean
public class DBProduct {
    private String itemNumber;
    private String englishName;
    private String hungarianName;
    private Map<String, Integer> actualStock;
    private Map<String, Integer> minStock;
    private Map<String, Integer> maxStock;
    private Map<String, Double> costs;
    private Integer lastSellingPrice;

    public DBProduct() {
    }

    public DBProduct(String itemNumber, String englishName, String hungarianName, Map<String, Integer> actualStock,
                     Map<String, Integer> minStock, Map<String, Integer> maxStock, Map<String, Double> costs,
                     Integer lastSellingPrice) {
        this.itemNumber = itemNumber;
        this.englishName = englishName;
        this.hungarianName = hungarianName;
        this.actualStock = actualStock;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.costs = costs;
        this.lastSellingPrice = lastSellingPrice;
    }

    @DynamoDbPartitionKey
    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getHungarianName() {
        return hungarianName;
    }

    public void setHungarianName(String hungarianName) {
        this.hungarianName = hungarianName;
    }

    public Map<String, Integer> getActualStock() {
        return actualStock;
    }

    public void setActualStock(Map<String, Integer> actualStock) {
        this.actualStock = actualStock;
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

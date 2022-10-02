package com.bench.stockmanagement.dataaccess;

import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Map;

@ToString
@DynamoDbBean
public class ProductDetail {
    private String attribute;
    private Map<String, Integer> minStock;
    private Map<String, Integer> maxStock;
    private Map<String, Integer> selling;
    private Map<String, Double> orders;

    public ProductDetail(){}

    public ProductDetail(String attribute, Map<String, Integer> minStock, Map<String, Integer> maxStock,
                         Map<String, Integer> selling, Map<String, Double> orders) {
        this.attribute = attribute;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.selling = selling;
        this.orders = orders;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
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

    public Map<String, Integer> getSelling() {
        return selling;
    }

    public void setSelling(Map<String, Integer> selling) {
        this.selling = selling;
    }

    public Map<String, Double> getOrders() {
        return orders;
    }

    public void setOrders(Map<String, Double> orders) {
        this.orders = orders;
    }
}

package com.bench.stockmanagement.dataaccess;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "Orders")
public class Order {
    private String id;
    private String date;
    private Double totalShippingCost;
    private Integer totalProductCount;
    private List<OrderedProduct> products;

    public Order(String id, String date, Double totalShippingCost,
                 Integer totalProductCount, List<OrderedProduct> products) {
        this.id = id;
        this.date = date;
        this.totalShippingCost = totalShippingCost;
        this.totalProductCount = totalProductCount;
        this.products = products;
    }

    @DynamoDBHashKey
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Double getTotalShippingCost() {
        return totalShippingCost;
    }

    public Integer getTotalProductCount() {
        return totalProductCount;
    }

    public List<OrderedProduct> getProducts() {
        return products;
    }

    public void setId(String id) {
        id = id;
    }

    public void setDate(String date) {
        date = date;
    }

    public void setTotalShippingCost(Double totalShippingCost) {
        this.totalShippingCost = totalShippingCost;
    }

    public void setTotalProductCount(Integer totalProductCount) {
        this.totalProductCount = totalProductCount;
    }

    public void setProducts(List<OrderedProduct> products) {
        this.products = products;
    }
}

package com.bench.stockmanagement.dataaccess;

import java.util.Map;

public class OrderDetails {
    private String englishName;
    private String attribute;
    private Map<String, Double> orders;

    public OrderDetails(String englishName, String attribute, Map<String, Double> orders) {
        this.englishName = englishName;
        this.attribute = attribute;
        this.orders = orders;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Map<String, Double> getOrders() {
        return orders;
    }

    public void setOrders(Map<String, Double> orders) {
        this.orders = orders;
    }
}

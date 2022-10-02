package com.bench.stockmanagement.dataaccess;

import com.bench.stockmanagement.domain.Order;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;
import java.util.Map;

@ToString
@DynamoDbBean
public class DBExtendedProduct {
    private String itemNumber;

    private Map<String, Integer> minStock;
    private Map<String, Integer> maxStock;

    private List<OrderDetails> orderDetails;
    private List<SellingDetails> sellingDetails;

    public DBExtendedProduct() {
    }

    public DBExtendedProduct(String itemNumber, Map<String, Integer> minStock, Map<String, Integer> maxStock,
                             List<OrderDetails> orderDetails, List<SellingDetails> sellingDetails) {
        this.itemNumber = itemNumber;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.orderDetails = orderDetails;
        this.sellingDetails = sellingDetails;
    }

    @DynamoDbPartitionKey
    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
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

    public List<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<SellingDetails> getSellingDetails() {
        return sellingDetails;
    }

    public void setSellingDetails(List<SellingDetails> sellingDetails) {
        this.sellingDetails = sellingDetails;
    }
}

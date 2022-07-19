package com.bench.stockmanagement.services.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.bench.stockmanagement.dataaccess.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderService {
    private final DynamoDBMapper mapper;

    @Autowired
    public OrderService() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:8000",
                "us-east-1");
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                                                           .withEndpointConfiguration(endpointConfiguration)
                                                           .build();
        this.mapper = new DynamoDBMapper(client);
    }

    public void saveOrder(List<Order> orders) {
        orders.forEach(mapper::save);
    }

    public Order getOrder(String id) {
        return mapper.load(Order.class, id);
    }

    public List<Order> getOrders() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(Order.class, scanExpression);
    }
}

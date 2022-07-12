package com.bench.stockmanagement.services.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.bench.stockmanagement.dataaccess.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public void saveOrder(Order order) {
        mapper.save(order);
    }
}

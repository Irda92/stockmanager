package com.bench.stockmanagement.services.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.bench.stockmanagement.dataaccess.SoldItem;
import com.bench.stockmanagement.services.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellingService {
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    @Autowired
    public SellingService() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:8000",
                "us-east-1");
        this.client = AmazonDynamoDBClientBuilder.standard()
                                                 .withEndpointConfiguration(endpointConfiguration)
                                                 .build();
        this.mapper = new DynamoDBMapper(client);
    }

    public void saveSoldItems(List<SoldItem> soldItems) {
        soldItems.forEach(mapper::save);
    }
}

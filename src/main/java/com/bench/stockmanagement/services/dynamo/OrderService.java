package com.bench.stockmanagement.services.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.bench.stockmanagement.dataaccess.DBOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void saveOrders(List<DBOrder> orders) {
        orders.forEach(mapper::save);
    }

    public PaginatedQueryList<DBOrder> getOrder(String seller, String date) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":seller",new AttributeValue().withS(seller));
        eav.put(":date", new AttributeValue().withS(date));

        DynamoDBQueryExpression<DBOrder> queryExpression = new DynamoDBQueryExpression<DBOrder>()
                .withIndexName("order_index")
                .withKeyConditionExpression("seller= :seller and orderDate= :date")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return mapper.query(DBOrder.class, queryExpression);
    }

    public PaginatedQueryList<DBOrder> getOrderedProductsByItemNumber(String itemNumber) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":itemNumber",new AttributeValue().withS(itemNumber));

        DynamoDBQueryExpression<DBOrder> queryExpression = new DynamoDBQueryExpression<DBOrder>()
                .withIndexName("product_index")
                .withKeyConditionExpression("itemNumber= :itemNumber")
                .withExpressionAttributeValues(eav)
                .withConsistentRead(false);

        return mapper.query(DBOrder.class, queryExpression);
    }

    public PaginatedScanList<DBOrder> getOrders() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(DBOrder.class, scanExpression);
    }
}

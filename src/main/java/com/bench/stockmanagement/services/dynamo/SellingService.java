package com.bench.stockmanagement.services.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.bench.stockmanagement.dataaccess.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SellingService {
    private final DynamoDBMapper mapper;

    @Autowired
    public SellingService() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                "http://localhost:8000",
                "us-east-1");
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                                                           .withEndpointConfiguration(endpointConfiguration)
                                                           .build();
        this.mapper = new DynamoDBMapper(client);
    }

    public void saveSoldItems(List<Receipt> soldItems) {
        soldItems.forEach(mapper::save);
    }

    public List<Receipt> getAReceipt(String receiptNumber) {
        Receipt queryItem = new Receipt();
        queryItem.setReceiptNumber(receiptNumber);

        DynamoDBQueryExpression<Receipt> query = new DynamoDBQueryExpression<Receipt>().withHashKeyValues(
                                                                                               queryItem)
                                                                                       .withLimit(10);
        return mapper.query(Receipt.class, query);
    }

    public List<Receipt> getAllReceipt() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        return mapper.scan(Receipt.class, scanExpression);
    }

    public List<Receipt> getSoldItems(String startDate, String endDate) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":startDate", new AttributeValue().withS(startDate));
        eav.put(":endDate", new AttributeValue().withS(endDate));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("sellingDate between :startDate and :endDate").withExpressionAttributeValues(eav);

        return mapper.scan(Receipt.class, scanExpression);
    }
}

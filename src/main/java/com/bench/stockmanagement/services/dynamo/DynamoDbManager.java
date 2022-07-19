package com.bench.stockmanagement.services.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.bench.stockmanagement.dataaccess.Order;
import com.bench.stockmanagement.dataaccess.Receipt;
import org.springframework.stereotype.Component;

@Component
public class DynamoDbManager {
    private static final ProvisionedThroughput PROVISIONED_THROUGHPUT = new ProvisionedThroughput().withReadCapacityUnits(
                                                                                                           10L)
                                                                                                   .withWriteCapacityUnits(
                                                                                                           5L);
    private static AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
            "http://localhost:8000",
            "us-east-1");
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                                                                      .withEndpointConfiguration(endpointConfiguration)
                                                                      .build();
    private static DynamoDB dynamoDB = new DynamoDB(client);

    public static void dropTable(String tableName) {
        Table table = dynamoDB.getTable(tableName);
        try {
            System.out.println("Issuing DeleteTable request for " + tableName);
            table.delete();

            table.waitForDelete();
        } catch (Exception e) {
            System.err.println("DeleteTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }

    public static void createOrderTable() {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(Order.class)
                                                      .withProvisionedThroughput(PROVISIONED_THROUGHPUT);

        TableUtils.createTableIfNotExists(client, createTableRequest);
    }

    public static void createSoldItemTable() {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        CreateTableRequest createTableRequest = mapper.generateCreateTableRequest(Receipt.class)
                                                      .withProvisionedThroughput(PROVISIONED_THROUGHPUT);

        TableUtils.createTableIfNotExists(client, createTableRequest);

    }
}

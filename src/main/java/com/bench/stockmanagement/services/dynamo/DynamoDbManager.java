package com.bench.stockmanagement.services.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class DynamoDbManager {
    private static final ProvisionedThroughput PROVISIONED_THROUGHPUT = new ProvisionedThroughput().withReadCapacityUnits(
                    10L)
            .withWriteCapacityUnits(
                    5L);
    private static final AwsClientBuilder.EndpointConfiguration ENDPOINT_CONFIGURATION = new AwsClientBuilder.EndpointConfiguration(
            "http://localhost:8000",
            "us-east-1");
    private static final AmazonDynamoDB CLIENT = AmazonDynamoDBClientBuilder.standard()
            .withEndpointConfiguration(ENDPOINT_CONFIGURATION)
            .build();
    private static final DynamoDB DYNAMO_DB = new DynamoDB(CLIENT);
    private static final DynamoDBMapper MAPPER = new DynamoDBMapper(CLIENT);

    public static void dropTable(String tableName) {
        Table table = DYNAMO_DB.getTable(tableName);
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
        CreateTableRequest createTableRequest = MAPPER.generateCreateTableRequest(DBOrder.class)
                .withProvisionedThroughput(PROVISIONED_THROUGHPUT);
        createTableRequest.getGlobalSecondaryIndexes().forEach(gsi -> {
            gsi.setProvisionedThroughput(PROVISIONED_THROUGHPUT);
            gsi.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        });

        TableUtils.createTableIfNotExists(CLIENT, createTableRequest);
    }

    public static void createSoldItemTable() {
        CreateTableRequest createTableRequest = MAPPER.generateCreateTableRequest(DBReceipt.class)
                .withProvisionedThroughput(PROVISIONED_THROUGHPUT);
        createTableRequest.getGlobalSecondaryIndexes().forEach(gsi -> {
            gsi.setProvisionedThroughput(PROVISIONED_THROUGHPUT);
            gsi.withProjection(new Projection().withProjectionType(ProjectionType.ALL));
        });
        TableUtils.createTableIfNotExists(CLIENT, createTableRequest);

    }

    public static void createProductTable() {
        CreateTableRequest createTableRequest = MAPPER.generateCreateTableRequest(Product.class)
                .withProvisionedThroughput(PROVISIONED_THROUGHPUT);
        TableUtils.createTableIfNotExists(CLIENT, createTableRequest);
    }
}

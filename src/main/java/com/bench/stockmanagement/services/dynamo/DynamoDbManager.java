package com.bench.stockmanagement.services.dynamo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DynamoDbManager {
    static AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
            "http://localhost:8000",
            "us-east-1");
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                                                              .withEndpointConfiguration(endpointConfiguration)
                                                              .build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    public static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
                                    String partitionKeyName, String partitionKeyType, String sortKeyName,
                                    String sortKeyType) {

        try {

            ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement().withAttributeName(partitionKeyName)
                                                .withKeyType(KeyType.HASH)); // Partition
            // key

            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions
                    .add(new AttributeDefinition().withAttributeName(partitionKeyName)
                                                  .withAttributeType(partitionKeyType));

            if (sortKeyName != null) {
                keySchema.add(new KeySchemaElement().withAttributeName(sortKeyName)
                                                    .withKeyType(KeyType.RANGE)); // Sort
                // key
                attributeDefinitions
                        .add(new AttributeDefinition().withAttributeName(sortKeyName)
                                                      .withAttributeType(sortKeyType));
            }

            CreateTableRequest request = new CreateTableRequest().withTableName(tableName)
                                                                 .withKeySchema(keySchema)
                                                                 .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(
                                                                                                                               readCapacityUnits)
                                                                                                                       .withWriteCapacityUnits(
                                                                                                                               writeCapacityUnits));

            request.setAttributeDefinitions(attributeDefinitions);

            System.out.println("Issuing CreateTable request for " + tableName);
            boolean tableIfNotExists = TableUtils.createTableIfNotExists(client, request);
//            Table table = dynamoDB.createTable(request);
            System.out.println(tableName + " already exists: " + tableIfNotExists);
            System.out.println("Waiting for " + tableName + " to be created...this may take a while...");
//            table.waitForActive();

        } catch (Exception e) {
            System.err.println("CreateTable request failed for " + tableName);
            System.err.println(e.getMessage());
        }
    }
}

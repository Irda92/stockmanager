package com.bench.stockmanagement;

import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.bench.stockmanagement.services.dynamo.DynamoDbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockmanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockmanagementApplication.class, args);
        System.out.println("Started...");

        DynamoDbManager.createTable("Orders", 10L, 5L, "id", "S", "date", "S");
        DynamoDbManager.createTable("SoldItems", 10L, 5L, "id", "S", "receiptNumber", "N");
    }
}

package com.bench.stockmanagement;

import com.bench.stockmanagement.services.Reader;
import com.bench.stockmanagement.services.dynamo.DynamoDbManager;
import com.bench.stockmanagement.services.dynamo.OrderService;
import com.bench.stockmanagement.services.dynamo.SellingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockmanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockmanagementApplication.class, args);

        //For local testing
        initializeDatabase(true);
    }

    private static void initializeDatabase(boolean shouldRun) {
        if (!shouldRun) {
            return;
        }
        DynamoDbManager.dropTable("Orders");
        DynamoDbManager.dropTable("SoldItems");

        DynamoDbManager.createOrderTable();
        DynamoDbManager.createSoldItemTable();

        Reader reader = new Reader();

        OrderHandler orderHandler = new OrderHandler(reader, new OrderService());
        orderHandler.loadOrder();

        SellingHandler sellingHandler = new SellingHandler(reader, new SellingService());

        sellingHandler.loadSoldItems();
    }
}

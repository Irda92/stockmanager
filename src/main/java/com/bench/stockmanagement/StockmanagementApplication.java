package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.dataaccess.DBProduct;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.services.dynamo.DynamoDbConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.model.Projection;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class StockmanagementApplication {
    private static final DynamoDbConfig DYNAMO_DB_CONFIG = new DynamoDbConfig("http://localhost:8000");
    private static final DynamoDbAsyncClient ASYNC_CLIENT = DYNAMO_DB_CONFIG.getDynamoDbAsyncClient();
    private static final DynamoDbEnhancedAsyncClient ENHANCED_ASYNC_CLIENT = DYNAMO_DB_CONFIG.getDynamoDbEnhancedAsyncClient();
    private static final Projection PROJECTION = Projection.builder().projectionType("ALL").build();

    public static void main(String... args) {
        SpringApplication.run(StockmanagementApplication.class, args);

        CompletableFuture<ListTablesResponse> listTablesResponseCompletableFuture = ASYNC_CLIENT.listTables();
        CompletableFuture<List<String>> listCompletableFuture = listTablesResponseCompletableFuture.thenApply(
                ListTablesResponse::tableNames);

        createReceiptTable(listCompletableFuture);
        createOrderTable(listCompletableFuture);
        createProductTable(listCompletableFuture);
    }

    private static void createReceiptTable(CompletableFuture<List<String>> listCompletableFuture) {
        listCompletableFuture.thenAccept(tables -> {
            if (null != tables && !tables.contains(Constants.RECEIPT_TABLE_NAME)) {
                DynamoDbAsyncTable<DBReceipt> receiptTable = ENHANCED_ASYNC_CLIENT.table(Constants.RECEIPT_TABLE_NAME,
                        TableSchema.fromBean(DBReceipt.class));

                EnhancedGlobalSecondaryIndex receiptIndex = createGsi("receipt_index");
                EnhancedGlobalSecondaryIndex soldItemIndex = createGsi("sold_item_index");

                CreateTableEnhancedRequest req = CreateTableEnhancedRequest.builder()
                        .globalSecondaryIndices(receiptIndex, soldItemIndex)
                        .build();

                receiptTable.createTable(req);
            }
        });
    }

    private static void createOrderTable(CompletableFuture<List<String>> listCompletableFuture) {
        listCompletableFuture.thenAccept(tables -> {
            if (null != tables && !tables.contains(Constants.ORDER_TABLE_NAME)) {
                DynamoDbAsyncTable<DBOrder> receiptTable = ENHANCED_ASYNC_CLIENT.table(Constants.ORDER_TABLE_NAME,
                        TableSchema.fromBean(DBOrder.class));

                EnhancedGlobalSecondaryIndex orderIndex = createGsi("order_index");
                EnhancedGlobalSecondaryIndex productIndex = createGsi("product_index");

                CreateTableEnhancedRequest req = CreateTableEnhancedRequest.builder()
                        .globalSecondaryIndices(orderIndex, productIndex)
                        .build();

                receiptTable.createTable(req);
            }
        });
    }

    private static void createProductTable(CompletableFuture<List<String>> listCompletableFuture) {
        listCompletableFuture.thenAccept(tables -> {
            if (null != tables && !tables.contains(Constants.PRODUCT_TABLE_NAME)) {
                DynamoDbAsyncTable<DBProduct> receiptTable = ENHANCED_ASYNC_CLIENT.table(Constants.PRODUCT_TABLE_NAME,
                        TableSchema.fromBean(DBProduct.class));

                receiptTable.createTable();
            }
        });
    }

    private static EnhancedGlobalSecondaryIndex createGsi(String index) {
        return EnhancedGlobalSecondaryIndex.builder()
                .indexName(index)
                .projection(PROJECTION)
                .build();
    }
}

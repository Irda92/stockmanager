package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.mappers.OrderMapper;
import com.bench.stockmanagement.mappers.SellingMapper;
import com.bench.stockmanagement.services.OrderReader;
import com.bench.stockmanagement.services.SellingReader;
import com.bench.stockmanagement.services.dynamo.DynamoDbConfig;
import com.bench.stockmanagement.services.dynamo.OrderService;
import com.bench.stockmanagement.services.dynamo.SellingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.CreateTableEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.ListTablesResponse;
import software.amazon.awssdk.services.dynamodb.model.Projection;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class StockmanagementApplication {

    private static final DynamoDbConfig dynamoDbConfig = new DynamoDbConfig("http://localhost:8000");
    private static final DynamoDbAsyncClient asyncClient = dynamoDbConfig.getDynamoDbAsyncClient();
    private static final DynamoDbEnhancedAsyncClient enhancedAsyncClient = dynamoDbConfig.getDynamoDbEnhancedAsyncClient();

    public static void main(String... args) {
        SpringApplication.run(StockmanagementApplication.class, args);

        createReceiptTable();
        createOrderTable();
    }

    private static void createReceiptTable() {
        CompletableFuture<ListTablesResponse> listTablesResponseCompletableFuture = asyncClient.listTables();
        CompletableFuture<List<String>> listCompletableFuture = listTablesResponseCompletableFuture.thenApply(
                ListTablesResponse::tableNames);
        listCompletableFuture.thenAccept(tables -> {
            if (null != tables && !tables.contains(DBReceipt.class.getSimpleName())) {
                DynamoDbAsyncTable<DBReceipt> receiptTable = enhancedAsyncClient.table(DBReceipt.class.getSimpleName(),
                        TableSchema.fromBean(DBReceipt.class));
                Projection projection = Projection.builder().projectionType("ALL").build();
                EnhancedGlobalSecondaryIndex receiptIndex = EnhancedGlobalSecondaryIndex.builder()
                        .indexName("receipt_index")
                        .projection(projection)
                        .build();
                EnhancedGlobalSecondaryIndex soldItemIndex = EnhancedGlobalSecondaryIndex.builder()
                        .indexName("sold_item_index")
                        .projection(projection)
                        .build();
                EnhancedGlobalSecondaryIndex receiptPeriodIndex = EnhancedGlobalSecondaryIndex.builder()
                        .indexName("receipt_period_index")
                        .projection(projection)
                        .build();
                CreateTableEnhancedRequest req = CreateTableEnhancedRequest.builder()
                        .globalSecondaryIndices(receiptIndex, soldItemIndex, receiptPeriodIndex)
                        .build();
                receiptTable.createTable(req);

            }
        });
    }

    private static void createOrderTable() {
        CompletableFuture<ListTablesResponse> listTablesResponseCompletableFuture = asyncClient.listTables();
        CompletableFuture<List<String>> listCompletableFuture = listTablesResponseCompletableFuture.thenApply(
                ListTablesResponse::tableNames);
        listCompletableFuture.thenAccept(tables -> {
            if (null != tables && !tables.contains(DBOrder.class.getSimpleName())) {
                DynamoDbAsyncTable<DBOrder> receiptTable = enhancedAsyncClient.table(DBOrder.class.getSimpleName(),
                        TableSchema.fromBean(DBOrder.class));
                Projection projection = Projection.builder().projectionType("ALL").build();
                EnhancedGlobalSecondaryIndex orderIndex = EnhancedGlobalSecondaryIndex.builder()
                        .indexName("order_index")
                        .projection(projection)
                        .build();
                EnhancedGlobalSecondaryIndex productIndex = EnhancedGlobalSecondaryIndex.builder()
                        .indexName("product_index")
                        .projection(projection)
                        .build();
                CreateTableEnhancedRequest req = CreateTableEnhancedRequest.builder()
                        .globalSecondaryIndices(orderIndex, productIndex)
                        .build();
                receiptTable.createTable(req);

            }
        });
    }

//    private static void loadSelling() {
//        SellingReader sellingReader = new SellingReader();
//        SellingMapper sellingMapper = new SellingMapper();
//        SellingService sellingService = new SellingService();
//
//        SellingHandler sellingHandler = new SellingHandler(sellingReader, sellingMapper, sellingService,
//                sellingRepository);
//
//        sellingHandler.loadSoldItems();
//    }
//
//    private static void loadOrders() {
//        OrderReader orderReader = new OrderReader();
//        OrderMapper orderMapper = new OrderMapper();
//        OrderService orderService = new OrderService();
//
//        OrderHandler orderHandler = new OrderHandler(orderReader, orderMapper, orderService);
//
//        orderHandler.loadOrder();
//    }
}

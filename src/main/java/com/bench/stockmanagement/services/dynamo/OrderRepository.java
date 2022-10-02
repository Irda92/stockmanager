package com.bench.stockmanagement.services.dynamo;

import com.bench.stockmanagement.Constants;
import com.bench.stockmanagement.dataaccess.DBOrder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.concurrent.CompletableFuture;

import static com.bench.stockmanagement.Constants.ORDER_TABLE_NAME;

@Component
public class OrderRepository {
    private final DynamoDbAsyncTable<DBOrder> table;
    private final DynamoDbAsyncIndex<DBOrder> orderIndex;
    private final DynamoDbAsyncIndex<DBOrder> productIndex;

    public OrderRepository(DynamoDbEnhancedAsyncClient client) {
        this.table = client.table(ORDER_TABLE_NAME, TableSchema.fromBean(DBOrder.class));
        this.orderIndex = this.table.index("order_index");
        this.productIndex = this.table.index("product_index");
    }

    public CompletableFuture<Void> saveOrders(DBOrder order) {
        return table.putItem(order);
    }

    public SdkPublisher<Page<DBOrder>> getOrderBySellerAndDate(String seller, String date) {
        QueryConditional query = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(seller).sortValue(date).build());

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(query)
                .consistentRead(false)
                .build();

        return orderIndex.query(request);
    }

    public SdkPublisher<Page<DBOrder>> getOrdersByItemNumber(String itemNumber) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(itemNumber)
                        .build()
        );
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .limit(2)
                .consistentRead(false)
                .build();
        return productIndex.query(request);
    }

    public PagePublisher<DBOrder> getAllOrders() {
        return table.scan();
    }
}

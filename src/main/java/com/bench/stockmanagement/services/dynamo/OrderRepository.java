package com.bench.stockmanagement.services.dynamo;

import com.bench.stockmanagement.dataaccess.DBOrder;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.concurrent.CompletableFuture;

@Component
public class OrderRepository {
    private final DynamoDbAsyncTable<DBOrder> table;
    private final DynamoDbAsyncIndex<DBOrder> orderIndex;
    private final DynamoDbAsyncIndex<DBOrder> productIndex;

    public OrderRepository(DynamoDbEnhancedAsyncClient client) {
        this.table = client.table(DBOrder.class.getSimpleName(), TableSchema.fromBean(DBOrder.class));
        this.orderIndex = client.table(DBOrder.class.getSimpleName(), TableSchema.fromBean(DBOrder.class))
                .index("order_index");
        this.productIndex = client.table(DBOrder.class.getSimpleName(), TableSchema.fromBean(DBOrder.class))
                .index("product_index");
    }

    public CompletableFuture<Void> saveOrders(DBOrder order) {
        return table.putItem(order);
    }
//
    public SdkPublisher<Page<DBOrder>> getOrderBySellerAndDate(String seller, String date) {
        QueryConditional query = QueryConditional.keyEqualTo(
                Key.builder().partitionValue(seller).sortValue(date).build());

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(query)
                .consistentRead(false)
                .build();

        return orderIndex.query(request);
    }
//        Map<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":seller",new AttributeValue().withS(seller));
//        eav.put(":date", new AttributeValue().withS(date));
//
//        DynamoDBQueryExpression<DBOrder> queryExpression = new DynamoDBQueryExpression<DBOrder>()
//                .withIndexName("order_index")
//                .withKeyConditionExpression("seller= :seller and orderDate= :date")
//                .withExpressionAttributeValues(eav)
//                .withConsistentRead(false);
//
//        return mapper.query(DBOrder.class, queryExpression);
//    }
//
    public SdkPublisher<Page<DBOrder>> getOrdersByItemNumber(String itemNumber) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(itemNumber)
                        .build()
        );
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional).consistentRead(false)
                .build();
        return productIndex.query(request);
    }
//        Map<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":itemNumber",new AttributeValue().withS(itemNumber));
//
//        DynamoDBQueryExpression<DBOrder> queryExpression = new DynamoDBQueryExpression<DBOrder>()
//                .withIndexName("product_index")
//                .withKeyConditionExpression("itemNumber= :itemNumber")
//                .withExpressionAttributeValues(eav)
//                .withConsistentRead(false);
//
//        return mapper.query(DBOrder.class, queryExpression);
//    }
//
    public PagePublisher<DBOrder> getAllOrders() {
        return table.scan();
    }
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//        return mapper.scan(DBOrder.class, scanExpression);
//    }
}

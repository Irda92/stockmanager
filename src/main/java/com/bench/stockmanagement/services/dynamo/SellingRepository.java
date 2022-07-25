package com.bench.stockmanagement.services.dynamo;

import com.bench.stockmanagement.dataaccess.DBReceipt;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Repository
public class SellingRepository {
    private final DynamoDbAsyncTable<DBReceipt> table;
    private final DynamoDbAsyncIndex<DBReceipt> receiptIndex;
    private final DynamoDbAsyncIndex<DBReceipt> soldItemIndex;
    private final DynamoDbAsyncIndex<DBReceipt> receiptPeriodIndex;

    public SellingRepository(DynamoDbEnhancedAsyncClient client) {
        this.table = client.table(DBReceipt.class.getSimpleName(), TableSchema.fromBean(DBReceipt.class));
        this.receiptIndex = client.table(DBReceipt.class.getSimpleName(), TableSchema.fromBean(DBReceipt.class))
                .index("receipt_index");
        this.soldItemIndex = client.table(DBReceipt.class.getSimpleName(), TableSchema.fromBean(DBReceipt.class))
                .index("sold_item_index");
        this.receiptPeriodIndex = client.table(DBReceipt.class.getSimpleName(), TableSchema.fromBean(DBReceipt.class))
                .index("receipt_period_index");
    }

    public CompletableFuture<Void> saveSoldItems(DBReceipt soldItems) {
        return table.putItem(soldItems);
    }

    public SdkPublisher<Page<DBReceipt>> getReceiptById(String receiptId) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(receiptId)
                        .build()
        );
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional).consistentRead(false)
                .build();
        return receiptIndex.query(request);
    }

    public PagePublisher<DBReceipt> getAllReceipt() {
        return table.scan();
    }

    public SdkPublisher<Page<DBReceipt>> getSoldItemsByItemNumber(String itemNumber) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(itemNumber)
                        .build()
        );
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional).consistentRead(false)
                .build();
        return soldItemIndex.query(request);
    }

    public SdkPublisher<Page<DBReceipt>> getSoldItemsByDate(String startDate, String endDate) {
        Expression myExpression = Expression.builder()
                .expression("#a between :b and :c")
                .putExpressionName("#a", "sellingDate")
                .putExpressionValue(":b", AttributeValue.builder().s(startDate).build())
                .putExpressionValue(":c", AttributeValue.builder().s(endDate).build())
                .build();

//        Map<String, AttributeValue> eav = new HashMap<>();
//        eav.put("#a",AttributeValue.fromS("sellingDate"));
//        eav.put(":b", AttributeValue.fromS(startDate));
//        eav.put(":c", AttributeValue.fromS(endDate));

//        QueryConditional queryConditional = QueryConditional.sortBetween(
//                Key.builder()
//                        .sortValue(startDate)
//                        .build(),
//                Key.builder()
//                        .sortValue(endDate)
//                        .build()
//        );
//        QueryRequest queryRequest = QueryRequest.builder()
//                .filterExpression("#a between :b and :c")
//                .expressionAttributeValues(eav)
//                .consistentRead(false)
//                .build();
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
//                .queryConditional(queryConditional)
                .filterExpression(myExpression)
                .consistentRead(false)
                .build();
        return receiptPeriodIndex.query(request);
    }
}

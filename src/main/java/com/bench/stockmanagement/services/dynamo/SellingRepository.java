package com.bench.stockmanagement.services.dynamo;

import com.bench.stockmanagement.dataaccess.DBReceipt;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.concurrent.CompletableFuture;

import static com.bench.stockmanagement.Constants.RECEIPT_TABLE_NAME;

@Repository
public class SellingRepository {
    private final DynamoDbAsyncTable<DBReceipt> table;
    private final DynamoDbAsyncIndex<DBReceipt> receiptIndex;
    private final DynamoDbAsyncIndex<DBReceipt> soldItemIndex;

    public SellingRepository(DynamoDbEnhancedAsyncClient client) {
        this.table = client.table(RECEIPT_TABLE_NAME, TableSchema.fromBean(DBReceipt.class));
        this.receiptIndex = this.table.index("receipt_index");
        this.soldItemIndex = this.table.index("sold_item_index");
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
                .queryConditional(queryConditional)
                .limit(1)   //just to see the difference between two solutions
                .consistentRead(false)
                .build();
        return soldItemIndex.query(request);
    }

    public SdkPublisher<Page<DBReceipt>> getSoldItemByDate(String itemNumber, String startDate, String endDate) {
        QueryConditional queryConditional = QueryConditional.sortBetween(
                Key.builder()
                        .partitionValue(itemNumber)
                        .sortValue(startDate)
                        .build(),
                Key.builder()
                        .partitionValue(itemNumber)
                        .sortValue(endDate)
                        .build()
        );

        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .consistentRead(false)
                .build();

        return soldItemIndex.query(request);
    }
}

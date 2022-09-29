package com.bench.stockmanagement.services.dynamo;

import com.bench.stockmanagement.dataaccess.DBProduct;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;

import java.util.concurrent.CompletableFuture;

import static com.bench.stockmanagement.Constants.PRODUCT_TABLE_NAME;

@Component
public class ProductRepository {
    private final DynamoDbAsyncTable<DBProduct> table;

    public ProductRepository(DynamoDbEnhancedAsyncClient client) {
        this.table = client.table(PRODUCT_TABLE_NAME, TableSchema.fromBean(DBProduct.class));
    }

    public CompletableFuture<Void> saveProducts(DBProduct product) {
        return table.putItem(product);
    }

    public PagePublisher<DBProduct> getAllProduct() {
        return table.scan();
    }

    public CompletableFuture<DBProduct> getProductByItemNumber(String itemNumber) {
        return table.getItem(Key.builder()
                .partitionValue(itemNumber)
                .build());
    }

    public CompletableFuture<DBProduct> updateProduct(DBProduct product) {
        return table.updateItem(product);
    }

}

package com.bench.stockmanagement.services.dynamo;

import org.springframework.stereotype.Component;

@Component
public class SellingService {
//    private final DynamoDBMapper mapper;
//
//    @Autowired
//    public SellingService() {
//        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
//                "http://localhost:8000",
//                "us-east-1");
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//                                                           .withEndpointConfiguration(endpointConfiguration)
//                                                           .build();
//        this.mapper = new DynamoDBMapper(client);
//    }
//
//    public void saveSoldItems(List<DBReceipt> soldItems) {
//        soldItems.forEach(mapper::save);
//    }
//
//    public PaginatedQueryList<DBReceipt> getAReceipt(String receiptNumber) {
//        Map<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":receiptNumber",new AttributeValue().withS(receiptNumber));
//
//        DynamoDBQueryExpression<DBReceipt> queryExpression = new DynamoDBQueryExpression<DBReceipt>()
//                .withIndexName("receipt_index")
//                .withKeyConditionExpression("receiptNumber= :receiptNumber")
//                .withExpressionAttributeValues(eav)
//                .withConsistentRead(false);
//
//        return mapper.query(DBReceipt.class, queryExpression);
//    }
//
//    public List<DBReceipt> getAllReceipt() {
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
//        return mapper.scan(DBReceipt.class, scanExpression);
//    }
//
//    public List<DBReceipt> getSoldItemsByDate(String startDate, String endDate) {
//        Map<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":startDate", new AttributeValue().withS(startDate));
//        eav.put(":endDate", new AttributeValue().withS(endDate));
//
//        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
//                .withIndexName("receipt_index")
//                .withFilterExpression("sellingDate between :startDate and :endDate").withExpressionAttributeValues(eav)
//                .withConsistentRead(false);
//
//        return mapper.scan(DBReceipt.class, scanExpression);
//    }
//
//    public List<DBReceipt> getSoldItemsByItemNumber(String itemNumber) {
//        Map<String, AttributeValue> eav = new HashMap<>();
//        eav.put(":itemNumber", new AttributeValue().withS(itemNumber));
//
//        DynamoDBQueryExpression<DBReceipt> scanExpression = new DynamoDBQueryExpression()
//                .withIndexName("sold_item_index")
//                .withKeyConditionExpression("itemNumber= :itemNumber")
//                .withExpressionAttributeValues(eav)
//                .withConsistentRead(false);
//
//        return mapper.query(DBReceipt.class, scanExpression);
//    }
}

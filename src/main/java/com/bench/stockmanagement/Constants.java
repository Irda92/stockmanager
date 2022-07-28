package com.bench.stockmanagement;

import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final DateTimeFormatter FORMATTER_WITH_DOTS = DateTimeFormatter.ofPattern("yyyy.MM.dd.");

    public static final String ORDER_TABLE_NAME = "Orders";
    public static final String RECEIPT_TABLE_NAME = "Receipts";
    public static final String PRODUCT_TABLE_NAME = "Products";
}

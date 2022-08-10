package com.bench.stockmanagement;

import java.time.format.DateTimeFormatter;

public final class Constants {
    public static final DateTimeFormatter FORMATTER_WITH_DOTS = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
}

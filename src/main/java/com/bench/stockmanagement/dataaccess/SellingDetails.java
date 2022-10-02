package com.bench.stockmanagement.dataaccess;

import java.util.Map;

public class SellingDetails {
    private String hungarianName;
    private String attribute;
    private Map<String, Integer> selling;

    public SellingDetails(String hungarianName, String attribute,
                          Map<String, Integer> selling)
    {
        this.hungarianName = hungarianName;
        this.attribute = attribute;
        this.selling = selling;
    }
}

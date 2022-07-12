package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.SoldItem;
import com.bench.stockmanagement.services.Reader;
import com.bench.stockmanagement.services.dynamo.SellingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellingHandler {
    private final Reader reader;
    private final SellingService sellingService;

    @Autowired
    public SellingHandler(Reader reader, SellingService sellingService) {
        this.reader = reader;
        this.sellingService = sellingService;
    }

    public void loadSoldItems(String fileName) {
        List<SoldItem> soldItems = reader.readSoldItems(fileName);
        sellingService.saveSoldItems(soldItems);
    }
}

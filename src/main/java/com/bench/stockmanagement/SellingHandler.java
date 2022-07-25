package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.Receipt;
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

    public void loadSoldItems() {
        List<Receipt> soldItems = reader.readSoldItems();
        sellingService.saveSoldItems(soldItems);
    }

    public List<Receipt> getReceipt(String receiptNumber) {
        return sellingService.getAReceipt(receiptNumber);
    }

    public List<Receipt> getAllReceipt() {
        return sellingService.getAllReceipt();
    }

    public List<Receipt> getSoldItemBetween(String startDate, String endDate) {
        return sellingService.getSoldItems(startDate, endDate);
    }
}

package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.Receipt;
import com.bench.stockmanagement.domain.SoldItem;
import com.bench.stockmanagement.mappers.SellingMapper;
import com.bench.stockmanagement.services.SellingReader;
import com.bench.stockmanagement.services.dynamo.SellingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SellingHandler {
    private final SellingReader reader;
    private final SellingMapper mapper;
    private final SellingService sellingService;

    @Autowired
    public SellingHandler(SellingReader reader, SellingMapper mapper, SellingService sellingService) {
        this.reader = reader;
        this.mapper = mapper;
        this.sellingService = sellingService;
    }

    public void loadSoldItems() {
        List<Receipt> soldItems = reader.readSoldItems();
        List<DBReceipt> dbReceipts = mapper.mapToDB(soldItems);
        sellingService.saveSoldItems(dbReceipts);
    }

    public Receipt getReceipt(String receiptNumber) {
        List<DBReceipt> receipts = sellingService.getAReceipt(receiptNumber);
        return mapper.mapReceipt(receipts);
    }

    public List<Receipt> getAllReceipt() {
        List<DBReceipt> allReceipt = sellingService.getAllReceipt();
        return mapper.mapReceipts(allReceipt);
    }

    public List<Receipt> getSoldItemBetween(String startDate, String endDate) {
        List<DBReceipt> soldItems = sellingService.getSoldItemsByDate(startDate, endDate);
        return mapper.mapReceipts(soldItems);
    }

    public List<SoldItem> getItems(String itemNumber) {
        List<DBReceipt> soldItemsByItemNumber = sellingService.getSoldItemsByItemNumber(itemNumber);
        return mapper.getItems(soldItemsByItemNumber);
    }
}

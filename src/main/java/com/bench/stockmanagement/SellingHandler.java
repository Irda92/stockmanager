package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.Receipt;
import com.bench.stockmanagement.domain.Result;
import com.bench.stockmanagement.domain.SoldItem;
import com.bench.stockmanagement.mappers.SellingMapper;
import com.bench.stockmanagement.services.SellingReader;
import com.bench.stockmanagement.services.dynamo.SellingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.List;

import static com.bench.stockmanagement.domain.Result.FAIL;
import static com.bench.stockmanagement.domain.Result.SUCCESS;

@Component
public class SellingHandler {
    private final SellingReader reader;
    private final SellingMapper mapper;
    private final SellingRepository sellingRepository;

    @Autowired
    public SellingHandler(SellingReader reader, SellingMapper mapper, SellingRepository sellingRepository) {
        this.reader = reader;
        this.mapper = mapper;
        this.sellingRepository = sellingRepository;
    }

    // Save sold items
    public Mono<Result> loadSoldItems() {
        List<Receipt> soldItems = reader.readSoldItems();
        List<DBReceipt> dbReceipts = mapper.mapToDB(soldItems);
        return Flux.fromIterable(dbReceipts)
                .doOnNext(sellingRepository::saveSoldItems)
                .then(Mono.just(SUCCESS))
                .onErrorReturn(FAIL);
    }

    // Get a receipt by receipt number
    public Mono<Receipt> getReceipt(String receiptNumber) {
        SdkPublisher<Page<DBReceipt>> receiptById = sellingRepository.getReceiptById(receiptNumber);
        return Flux.from(receiptById)
                .log()
                .map(Page::items)
                .map(mapper::mapReceipt)
                .singleOrEmpty();
    }

    // Get all receipt
    public Flux<Receipt> getAllReceipt() {
        return Flux.from(sellingRepository.getAllReceipt()).log().map(Page::items).flatMapIterable(mapper::mapReceipts);
    }

    // Get items from all receipts by item number
    public Flux<SoldItem> getItems(String itemNumber) {
        return Flux.from(sellingRepository.getSoldItemsByItemNumber(itemNumber)).log()
                .map(Page::items)
                .flatMapIterable(mapper::getItems);
    }

    // Get receipts between dates which contain the given item numbers
    public Flux<Receipt> getSoldItemBetween(String startDate, String endDate, String... itemNumber) {
        return Flux.fromIterable(List.of(itemNumber)).log()
                .map(in -> sellingRepository.getSoldItemByDate(in, startDate, endDate))
                .flatMap(Flux::from)
                .map(Page::items)
                .flatMapIterable(mapper::mapReceipts);
    }
}

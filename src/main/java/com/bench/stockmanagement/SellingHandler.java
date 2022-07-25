package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.Receipt;
import com.bench.stockmanagement.domain.Result;
import com.bench.stockmanagement.domain.SoldItem;
import com.bench.stockmanagement.mappers.SellingMapper;
import com.bench.stockmanagement.services.SellingReader;
import com.bench.stockmanagement.services.dynamo.SellingRepository;
import com.bench.stockmanagement.services.dynamo.SellingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.List;

import static com.bench.stockmanagement.domain.Result.FAIL;
import static com.bench.stockmanagement.domain.Result.SUCCESS;

@Component
public class SellingHandler {
    private final SellingReader reader;
    private final SellingMapper mapper;
    private final SellingService sellingService;
    private final SellingRepository sellingRepository;

    @Autowired
    public SellingHandler(SellingReader reader, SellingMapper mapper, SellingService sellingService, SellingRepository sellingRepository) {
        this.reader = reader;
        this.mapper = mapper;
        this.sellingService = sellingService;
        this.sellingRepository = sellingRepository;
    }

    public Mono<Result> loadSoldItems() {
        List<Receipt> soldItems = reader.readSoldItems();
        List<DBReceipt> dbReceipts = mapper.mapToDB(soldItems);
        return Flux.fromIterable(dbReceipts)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(sellingRepository::saveSoldItems)
                .then(Mono.just(SUCCESS))
                .onErrorReturn(FAIL);
    }

    public Mono<Receipt> getReceipt(String receiptNumber) {
        SdkPublisher<Page<DBReceipt>> receiptById = sellingRepository.getReceiptById(receiptNumber);
        return Flux.from(receiptById)
                .log()
                .subscribeOn(Schedulers.boundedElastic())
                .map(Page::items)
                .map(mapper::mapReceipt)
                .singleOrEmpty();
//        return Flux.just(Receipt.EMPTY);
    }

    public Flux<Receipt> getAllReceipt() {
        return Flux.from(sellingRepository.getAllReceipt()).log().map(Page::items).flatMapIterable(mapper::mapReceipts);
    }

    public Flux<SoldItem> getItems(String itemNumber) {
        return Flux.from(sellingRepository.getSoldItemsByItemNumber(itemNumber)).log()
                .map(Page::items)
                .flatMapIterable(mapper::getItems);
    }

    public Flux<Receipt> getSoldItemBetween(String startDate, String endDate) {
        return Flux.from(sellingRepository.getSoldItemsByDate(startDate, endDate)).log()
                .map(Page::items)
                .flatMapIterable(mapper::mapReceipts);
    }
//
}

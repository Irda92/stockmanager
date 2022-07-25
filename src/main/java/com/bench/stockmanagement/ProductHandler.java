package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.Product;
import com.bench.stockmanagement.mappers.ProductMapper;
import com.bench.stockmanagement.services.dynamo.OrderRepository;
import com.bench.stockmanagement.services.dynamo.SellingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.List;

@Component
public class ProductHandler {
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;
    private final SellingRepository sellingRepository;

    @Autowired
    public ProductHandler(ProductMapper productMapper, OrderRepository orderRepository, SellingRepository sellingRepository) {
        this.productMapper = productMapper;
        this.orderRepository = orderRepository;
        this.sellingRepository = sellingRepository;
    }

    public Mono<Product> getProduct(String itemNumber) {
        SdkPublisher<Page<DBOrder>> ordersByItemNumber = orderRepository.getOrdersByItemNumber(itemNumber);
        SdkPublisher<Page<DBReceipt>> soldItemsByItemNumber = sellingRepository.getSoldItemsByItemNumber(itemNumber);

        Flux<List<DBOrder>> ordersFlux = Flux.from(ordersByItemNumber)
                .log()
                .subscribeOn(Schedulers.boundedElastic())
                .map(Page::items);

        Flux<List<DBReceipt>> receiptsFlux = Flux.from(soldItemsByItemNumber)
                .log()
                .subscribeOn(Schedulers.boundedElastic())
                .map(Page::items);

        return Flux.zip(ordersFlux, receiptsFlux)
                .map(objects -> productMapper.map(itemNumber, objects.getT1(), objects.getT2())).singleOrEmpty();
    }

}

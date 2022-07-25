package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.domain.Order;
import com.bench.stockmanagement.domain.OrderedProduct;
import com.bench.stockmanagement.domain.Result;
import com.bench.stockmanagement.mappers.OrderMapper;
import com.bench.stockmanagement.services.OrderReader;
import com.bench.stockmanagement.services.dynamo.OrderRepository;
import com.bench.stockmanagement.services.dynamo.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderHandler {
    private final OrderReader reader;
    private final OrderMapper mapper;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderHandler(OrderReader reader, OrderMapper mapper, OrderService orderService, OrderRepository orderRepository) {
        this.reader = reader;
        this.mapper = mapper;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    public Mono<Result> loadOrders() {
        List<Order> orders = reader.readOrder();
        List<DBOrder> dbOrders = orders.stream()
                .map(mapper::mapOrder)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return Flux.fromIterable(dbOrders)
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(orderRepository::saveOrders)
                .then(Mono.just(Result.SUCCESS))
                .onErrorReturn(Result.FAIL);
    }

    public Flux<Order> getAllOrders() {
        return Flux.from(orderRepository.getAllOrders()).log().map(Page::items).flatMapIterable(mapper::mapOrders);
    }

    public Mono<Order> getOrder(String seller, String date) {
        SdkPublisher<Page<DBOrder>> orderBySellerAndDate = orderRepository.getOrderBySellerAndDate(seller, date);
        return Flux.from(orderBySellerAndDate)
                .log()
                .subscribeOn(Schedulers.boundedElastic())
                .map(Page::items)
                .map(mapper::mapOrder)
                .onErrorReturn(Order.EMPTY)
                .singleOrEmpty();
    }

    public Flux<OrderedProduct> getOrderedProduct(String itemNumber) {
        return Flux.from(orderRepository.getOrdersByItemNumber(itemNumber))
                .log()
                .map(Page::items)
                .flatMapIterable(mapper::getProducts);
    }
}

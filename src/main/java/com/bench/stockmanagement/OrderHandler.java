package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.domain.Order;
import com.bench.stockmanagement.domain.OrderedProduct;
import com.bench.stockmanagement.domain.Result;
import com.bench.stockmanagement.mappers.OrderMapper;
import com.bench.stockmanagement.services.OrderReader;
import com.bench.stockmanagement.services.dynamo.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderHandler {
    private final OrderReader reader;
    private final OrderMapper mapper;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderHandler(OrderReader reader, OrderMapper mapper, OrderRepository orderRepository) {
        this.reader = reader;
        this.mapper = mapper;
        this.orderRepository = orderRepository;
    }

    //Save orders read from files
    public Mono<Result> loadOrders() {
        List<Order> orders = reader.readOrder();
        List<DBOrder> dbOrders = orders.stream()
                .map(mapper::mapOrder)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return Flux.fromIterable(dbOrders)
                .doOnNext(orderRepository::saveOrders)
                .collectList()
                .thenReturn(Result.SUCCESS)
                .onErrorReturn(Result.FAIL);
    }

    // Query all orders
    public Flux<Order> getAllOrders() {
        return Flux.from(orderRepository.getAllOrders()).log().map(Page::items).flatMapIterable(mapper::mapOrders);
    }

    // Get an order by the seller and the date
    public Mono<Order> getOrder(String seller, String date) {
        SdkPublisher<Page<DBOrder>> orderBySellerAndDate = orderRepository.getOrderBySellerAndDate(seller, date);
        return Flux.from(orderBySellerAndDate)
                .log()
                .map(Page::items)
                .map(mapper::mapOrder)
                .onErrorReturn(Order.EMPTY)
                .singleOrEmpty();
    }

    // Get the ordered quantity for an itemNumber
    public Flux<OrderedProduct> getOrderedProduct(String itemNumber) {
        return Flux.from(orderRepository.getOrdersByItemNumber(itemNumber))
                .log()
                .map(Page::items)
                .flatMapIterable(mapper::getProducts);
    }
}

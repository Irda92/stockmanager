package com.bench.stockmanagement;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.domain.Order;
import com.bench.stockmanagement.domain.OrderedProduct;
import com.bench.stockmanagement.mappers.OrderMapper;
import com.bench.stockmanagement.services.OrderReader;
import com.bench.stockmanagement.services.dynamo.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderHandler {
    private final OrderReader reader;
    private final OrderMapper mapper;
    private final OrderService orderService;

    @Autowired
    public OrderHandler(OrderReader reader, OrderMapper mapper, OrderService orderService) {
        this.reader = reader;
        this.mapper = mapper;
        this.orderService = orderService;
    }

    public void loadOrder() {
        List<Order> orders = reader.readOrder();
        List<DBOrder> dbOrders = orders.stream().map(mapper::mapOrder).flatMap(List::stream).collect(Collectors.toList());

        orderService.saveOrders(dbOrders);
    }

    public Order getOrder(String seller, String date) {
        PaginatedQueryList<DBOrder> order = orderService.getOrder(seller, date);
        return mapper.mapOrder(order);
    }

    public List<OrderedProduct> getOrderedProduct(String itemNumber) {
        PaginatedQueryList<DBOrder> orders = orderService.getOrderedProductsByItemNumber(itemNumber);
        return mapper.getProducts(orders);
    }

    public List<Order> getOrders() {
        List<DBOrder> orders = orderService.getOrders();
        return mapper.mapOrders(orders);
    }
}

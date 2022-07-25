package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.Order;
import com.bench.stockmanagement.services.Reader;
import com.bench.stockmanagement.services.dynamo.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderHandler {
    private final Reader reader;
    private final OrderService orderService;

    @Autowired
    public OrderHandler(Reader reader, OrderService orderService) {
        this.reader = reader;
        this.orderService = orderService;
    }

    public void loadOrder() {
        List<Order> order = reader.readOrder();
        orderService.saveOrder(order);
    }

    //TODO do not return the dataaccess Order
    public Order getOrder(String orderId) {
        return orderService.getOrder(orderId);
    }

    public List<Order> getOrders() {
        return orderService.getOrders();
    }
}

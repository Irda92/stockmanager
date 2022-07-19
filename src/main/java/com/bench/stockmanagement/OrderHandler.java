package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.Order;
import com.bench.stockmanagement.services.Reader;
import com.bench.stockmanagement.services.dynamo.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderHandler {
    private final Reader reader;
    private final OrderService orderService;

    @Autowired
    public OrderHandler(Reader reader, OrderService orderService) {
        this.reader = reader;
        this.orderService = orderService;
    }

    public void loadOrder(String fileName) {
        Order order = reader.readOrder(fileName);
        orderService.saveOrder(order);
    }
}

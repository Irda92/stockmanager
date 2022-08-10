package com.bench.stockmanagement;

import com.bench.stockmanagement.mappers.OrderMapper;
import com.bench.stockmanagement.mappers.SellingMapper;
import com.bench.stockmanagement.services.OrderReader;
import com.bench.stockmanagement.services.SellingReader;
import com.bench.stockmanagement.services.dynamo.OrderService;
import com.bench.stockmanagement.services.dynamo.SellingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockmanagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockmanagementApplication.class, args);

    }

    private static void loadSelling() {
        SellingReader sellingReader = new SellingReader();
        SellingMapper sellingMapper = new SellingMapper();
        SellingService sellingService = new SellingService();

        SellingHandler sellingHandler = new SellingHandler(sellingReader, sellingMapper, sellingService);

        sellingHandler.loadSoldItems();
    }

    private static void loadOrders() {
        OrderReader orderReader = new OrderReader();
        OrderMapper orderMapper = new OrderMapper();
        OrderService orderService = new OrderService();

        OrderHandler orderHandler = new OrderHandler(orderReader, orderMapper, orderService);

        orderHandler.loadOrder();
    }
}

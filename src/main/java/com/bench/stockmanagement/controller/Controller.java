package com.bench.stockmanagement.controller;

import com.bench.stockmanagement.OrderHandler;
import com.bench.stockmanagement.ProductHandler;
import com.bench.stockmanagement.SellingHandler;
import com.bench.stockmanagement.dataaccess.Order;
import com.bench.stockmanagement.dataaccess.Receipt;
import com.bench.stockmanagement.domain.Product;
import com.bench.stockmanagement.services.RateExchanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/testing")
public class Controller {
    private final RateExchanger rateExchanger;
    private final OrderHandler orderHandler;
    private final SellingHandler sellingHandler;
    private final ProductHandler productHandler;

    @Autowired
    public Controller(RateExchanger rateExchanger, OrderHandler orderHandler,
                      SellingHandler sellingHandler, ProductHandler productHandler) {
        this.rateExchanger = rateExchanger;
        this.orderHandler = orderHandler;
        this.sellingHandler = sellingHandler;
        this.productHandler = productHandler;
    }

    @GetMapping("/rate-exchange/{date}")
    public Double getExchangeRate(@PathVariable String date) {
        LocalDate inputDate = LocalDate.parse(date);
        return rateExchanger.getRateFor(inputDate);
    }

//    Probably these endpoints will be used later
//    @GetMapping("/save/order")
//    public String saveOrder() {
//        orderHandler.loadOrder();
//        return "Orders saved!";
//    }
//
//    @GetMapping("/save/soldItems}")
//    public String saveSoldProducts() {
//        sellingHandler.loadSoldItems();
//        return "Sold items saved!";
//    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderHandler.getOrders();
    }

    @GetMapping("/orders/{orderId}")
    public Order getAnOrder(@PathVariable String orderId) {
        return orderHandler.getOrder(orderId);
    }

    @GetMapping("/products")
    public List<Receipt> getAllReceipts() {
        return sellingHandler.getAllReceipt();
    }

    @GetMapping("/products")
    public List<Receipt> getItemsBetween(@RequestParam String startDate, @RequestParam String endDate) {
        return sellingHandler.getSoldItemBetween(startDate, endDate);
    }

    @GetMapping("/products/{receiptNumber}")
    public List<Receipt> getReceiptItems(@PathVariable String receiptNumber) {
        return sellingHandler.getReceipt(receiptNumber);
    }

    @GetMapping("/products/item/{itemNumber}")
    public Product getProduct(@PathVariable String itemNumber) {
        return productHandler.getProduct(itemNumber);
    }
}

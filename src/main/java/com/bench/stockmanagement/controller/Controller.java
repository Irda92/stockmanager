package com.bench.stockmanagement.controller;

import com.bench.stockmanagement.OrderHandler;
import com.bench.stockmanagement.ProductHandler;
import com.bench.stockmanagement.SellingHandler;
import com.bench.stockmanagement.domain.*;
import com.bench.stockmanagement.services.RateExchanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    //TODO input validation missing

//    @GetMapping("/rate-exchange/{date}")
//    public Mono<Double> getExchangeRate(@PathVariable String date) {
//        LocalDate inputDate = LocalDate.parse(date);
//        return rateExchanger.getRateFor(inputDate);
//    }

    @GetMapping("/save/order")
    public Mono<Result> saveOrder() {
        return orderHandler.loadOrders();
    }
//
    @GetMapping("/save/soldItems")
    public Mono<Result> saveSoldProducts() {
        return sellingHandler.loadSoldItems();
    }

    @GetMapping("/orders")
    public Flux<Order> getOrders() {
        return orderHandler.getAllOrders();
    }

    @GetMapping("/orders/{seller}")
    public Mono<Order> getAnOrder(@PathVariable String seller, @RequestParam String orderDate) {
        return orderHandler.getOrder(seller, orderDate);
    }

    @GetMapping("/orders/product/{itemNumber}")
    public Flux<OrderedProduct> getAnOrder(@PathVariable String itemNumber) {
        return orderHandler.getOrderedProduct(itemNumber);
    }

    @GetMapping("/selling/receipt")
    public Flux<Receipt> getAllReceipts() {
        return sellingHandler.getAllReceipt();
    }

    @GetMapping("/selling")
    public Flux<Receipt> getItemsBetween(@RequestParam String startDate, @RequestParam String endDate) {
        return sellingHandler.getSoldItemBetween(startDate, endDate);
    }

    @GetMapping("/selling/receipt/{receiptNumber}")
    public Mono<Receipt> getReceiptItems(@PathVariable String receiptNumber) {
        return sellingHandler.getReceipt(receiptNumber);
    }

    @GetMapping("/selling/item/{itemNumber}")
    public Flux<SoldItem> getSoldItems(@PathVariable String itemNumber) {
        return sellingHandler.getItems(itemNumber);
    }

    @GetMapping("/product/{itemNumber}")
    public Mono<Product> getProduct(@PathVariable String itemNumber) {
        return productHandler.getProduct(itemNumber);
    }
}

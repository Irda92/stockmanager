package com.bench.stockmanagement.controller;

import com.bench.stockmanagement.ReactiveOrderHandler;
import com.bench.stockmanagement.ReactiveProductHandler;
import com.bench.stockmanagement.ReactiveSellingHandler;
import com.bench.stockmanagement.domain.*;
import com.bench.stockmanagement.services.RateExchanger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/testing")
@CrossOrigin(origins="http://localhost:3000")
public class ReactiveController
{
    private final RateExchanger rateExchanger;
    private final ReactiveOrderHandler reactiveOrderHandler;
    private final ReactiveSellingHandler reactiveSellingHandler;
    private final ReactiveProductHandler reactiveProductHandler;

    @Autowired
    public ReactiveController(RateExchanger rateExchanger, ReactiveOrderHandler reactiveOrderHandler,
                              ReactiveSellingHandler reactiveSellingHandler, ReactiveProductHandler reactiveProductHandler) {
        this.rateExchanger = rateExchanger;
        this.reactiveOrderHandler = reactiveOrderHandler;
        this.reactiveSellingHandler = reactiveSellingHandler;
        this.reactiveProductHandler = reactiveProductHandler;
    }

    //TODO input validation missing

    @GetMapping("/save/order")
    public Mono<Result> saveOrder() {
        return reactiveOrderHandler.loadOrders();
    }

    @GetMapping("/save/soldItems")
    public Mono<Result> saveSoldProducts() {
        return reactiveSellingHandler.loadSoldItems();
    }

    @GetMapping("/save/products")
    public Mono<Result> saveProducts() {
        return reactiveProductHandler.saveProducts();
    }

    @GetMapping("/orders")
    public Flux<Order> getOrders() {
        return reactiveOrderHandler.getAllOrders();
    }

    @GetMapping("/orders/{seller}")
    public Mono<Order> getAnOrder(@PathVariable String seller, @RequestParam String orderDate) {
        return reactiveOrderHandler.getOrder(seller, orderDate);
    }

    @GetMapping("/orders/product/{itemNumber}")
    public Flux<OrderedProduct> getAnOrder(@PathVariable String itemNumber) {
        return reactiveOrderHandler.getOrderedProduct(itemNumber);
    }

    @GetMapping("/selling/receipt")
    public Flux<Receipt> getAllReceipts() {
        return reactiveSellingHandler.getAllReceipt();
    }

    @GetMapping("/selling")
    public Flux<Receipt> getItemsBetween(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String... itemNumber) {
        return reactiveSellingHandler.getSoldItemBetween(startDate, endDate, itemNumber);
    }

    @GetMapping("/selling/receipt/{receiptNumber}")
    public Mono<Receipt> getReceiptItems(@PathVariable String receiptNumber) {
        return reactiveSellingHandler.getReceipt(receiptNumber);
    }

    @GetMapping("/selling/item/{itemNumber}")
    public Flux<SoldItem> getSoldItems(@PathVariable String itemNumber) {
        return reactiveSellingHandler.getItems(itemNumber);
    }

//    @GetMapping("/product/{itemNumber}")
//    public Mono<Product> getProduct(@PathVariable String itemNumber) {
//        return reactiveProductHandler.getProduct(itemNumber);
//    }
//
//    @GetMapping("/product/all")
//    public Flux<Product> getAllProduct() {
//        return reactiveProductHandler.getAllProduct();
//    }
//
//    @GetMapping("/product/all/in-a-period")
//    public Flux<Product> getAllProduct(@RequestParam String startDate, @RequestParam String endDate) {
//        return reactiveProductHandler.getAllSoldItemBetween(startDate, endDate);
//    }
//
//    @GetMapping("/product/stock")
//    public Flux<ProductStockData> getAllProductStockData() {
//        return reactiveProductHandler.getAllProductStockData();
//    }
//
//    @GetMapping("/product/stock/{itemNumber}")
//    public Mono<List<ProductStockData>> getProductStockData(@PathVariable String itemNumber) {
//        return reactiveProductHandler.getProductByItemNumber(itemNumber);
//    }
//
//    @PostMapping("update/product/stock")
//    public Mono<Result> updateProductStockData(@RequestBody ProductStockData productStockData) {
//        return reactiveProductHandler.updateProduct(productStockData);
//    }
}

package com.bench.stockmanagement.controller;

import com.bench.stockmanagement.OrderHandler;
import com.bench.stockmanagement.ProductManager;
import com.bench.stockmanagement.SellingHandler;
import com.bench.stockmanagement.services.RateExchanger;
import com.bench.stockmanagement.services.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/testing")
public class Controller {
    private final RateExchanger rateExchanger;
    private final OrderHandler orderHandler;
    private final SellingHandler sellingHandler;

    @Autowired
    public Controller(RateExchanger rateExchanger, OrderHandler orderHandler,
                      SellingHandler sellingHandler) {
        this.rateExchanger = rateExchanger;
        this.orderHandler = orderHandler;
        this.sellingHandler = sellingHandler;
    }

    @GetMapping("/rate-exchange/{date}")
    public Double getExchangeRate(@PathVariable String date) {
        LocalDate inputDate = LocalDate.parse(date);
        return rateExchanger.getRateFor(inputDate);
    }

    @GetMapping("/orders/{fileName}")
    public String saveOrder(@PathVariable String fileName) {
        orderHandler.loadOrder(fileName);
        return "Orders saved!";
    }

    @GetMapping("/sold/{fileName}")
    public String saveSoldProducts(@PathVariable String fileName) {
        sellingHandler.loadSoldItems(fileName);
        return "Sold items saved!";
    }
}

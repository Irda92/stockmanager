package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.Product;
import com.bench.stockmanagement.mappers.ProductMapper;
import com.bench.stockmanagement.services.OrderReader;
import com.bench.stockmanagement.services.SellingReader;
import com.bench.stockmanagement.services.dynamo.OrderService;
import com.bench.stockmanagement.services.dynamo.SellingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductHandler {
    private final ProductMapper productMapper;
    private final OrderService orderService;
    private final SellingService sellingService;

    @Autowired
    public ProductHandler(ProductMapper productMapper, OrderService orderService, SellingService sellingService) {
        this.productMapper = productMapper;
        this.orderService = orderService;
        this.sellingService = sellingService;
    }

    public Product getProduct(String itemNumber) {
        List<DBOrder> orderedProducts = orderService.getOrderedProductsByItemNumber(itemNumber);
        List<DBReceipt> soldItems = sellingService.getSoldItemsByItemNumber(itemNumber);
        return productMapper.map(itemNumber, orderedProducts, soldItems);
    }

}

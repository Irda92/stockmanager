package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.dataaccess.DBProduct;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.Order;
import com.bench.stockmanagement.domain.Product;
import com.bench.stockmanagement.domain.ProductStockData;
import com.bench.stockmanagement.domain.Result;
import com.bench.stockmanagement.mappers.ProductMapper;
import com.bench.stockmanagement.services.OrderReader;
import com.bench.stockmanagement.services.dynamo.OrderRepository;
import com.bench.stockmanagement.services.dynamo.ProductRepository;
import com.bench.stockmanagement.services.dynamo.SellingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;

import java.util.*;

import static com.bench.stockmanagement.domain.Result.FAIL;
import static com.bench.stockmanagement.domain.Result.SUCCESS;

@Component
public class ProductHandler {
    private final OrderReader reader;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;
    private final SellingRepository sellingRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductHandler(OrderReader reader, ProductMapper productMapper, OrderRepository orderRepository,
            SellingRepository sellingRepository, ProductRepository productRepository) {
        this.reader = reader;
        this.productMapper = productMapper;
        this.orderRepository = orderRepository;
        this.sellingRepository = sellingRepository;
        this.productRepository = productRepository;
    }

    // Save products
    public Mono<Result> saveProducts() {
        List<Order> orders = reader.readOrder();
        List<DBProduct> dbProducts = productMapper.mapProduct(orders);

        return Flux.fromIterable(dbProducts).log()
                .doOnNext(productRepository::saveProducts)
                .then(Mono.just(SUCCESS))
                .onErrorReturn(FAIL);
    }

    // Get product by itemNumber
    // Item number, how many is expected in stock min/max, purchase prices
    public Mono<List<ProductStockData>> getProductByItemNumber(String itemNumber) {
        return Mono.fromFuture(() -> productRepository.getProductByItemNumber(itemNumber))
                .log()
                .map(productMapper::mapStockData);
    }

    // Get all information about a product by itemNumber
    // Name, quantity, price, all orders, all selling
    public Mono<Product> getProduct(String itemNumber) {
        SdkPublisher<Page<DBOrder>> ordersByItemNumber = orderRepository.getOrdersByItemNumber(itemNumber);
        SdkPublisher<Page<DBReceipt>> soldItemsByItemNumber = sellingRepository.getSoldItemsByItemNumber(itemNumber);

        Flux<DBOrder> ordersFlux = Flux.from(ordersByItemNumber)
                .log()
                .flatMapIterable(Page::items);

        Flux<DBReceipt> receiptsFlux = Flux.from(soldItemsByItemNumber)
                .log()
                .flatMapIterable(Page::items);

        return Mono.zip(ordersFlux.collectList(),
                        receiptsFlux.collectList()).log()
                .map(objects -> productMapper.map(itemNumber, objects.getT1(), objects.getT2()));
    }

    // Update the purchase price, stock information
    public Mono<Result> updateProduct(ProductStockData productStockData) {
        DBProduct dbProduct = productMapper.mapProduct(productStockData);
        return Mono.fromFuture(productRepository.getProductByItemNumber(productStockData.getItemNumber()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not find product for item number")))
                .flatMap(product -> Mono.fromFuture(productRepository.updateProduct(mergeDbProducts(product, dbProduct))))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not update product for item number")))
                .thenReturn(SUCCESS)
                .onErrorReturn(FAIL);
    }

    private DBProduct mergeDbProducts(DBProduct oldProduct, DBProduct newProduct) {
        String itemNumber = oldProduct.getItemNumber();

        Integer lastSellingPrice = Optional.ofNullable(newProduct.getLastSellingPrice())
                .orElse(oldProduct.getLastSellingPrice());

        Map<String, Double> costs = new HashMap<>();
        costs.putAll(oldProduct.getCosts());
        if (newProduct.getCosts() != null) {
            costs.putAll(newProduct.getCosts());
        }

        Map<String, Integer> minStock = getStockMap(oldProduct.getMinStock(), newProduct.getMinStock());
        Map<String, Integer> maxStock = getStockMap(oldProduct.getMaxStock(), newProduct.getMaxStock());

        return new DBProduct(itemNumber, lastSellingPrice, minStock, maxStock, costs);
    }

    private Map<String, Integer> getStockMap(Map<String, Integer> oldStock, Map<String, Integer> newStock) {
        Map<String, Integer> stock = new HashMap<>();
        stock.putAll(oldStock);

        if (newStock != null) {
            stock.putAll(newStock);
        }
        return stock;
    }
}

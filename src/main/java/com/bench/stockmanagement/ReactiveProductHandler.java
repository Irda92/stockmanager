package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.dataaccess.DBProduct;
import com.bench.stockmanagement.dataaccess.DBReceipt;
import com.bench.stockmanagement.domain.*;
import com.bench.stockmanagement.mappers.ProductMapper;
import com.bench.stockmanagement.services.OrderReader;
import com.bench.stockmanagement.services.SellingReader;
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
public class ReactiveProductHandler
{
    private final OrderReader reader;
    private final SellingReader sellingReader;
    private final ProductMapper productMapper;
    private final OrderRepository orderRepository;
    private final SellingRepository sellingRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReactiveProductHandler(OrderReader reader, SellingReader sellingReader, ProductMapper productMapper,
                                  OrderRepository orderRepository, SellingRepository sellingRepository,
                                  ProductRepository productRepository) {
        this.reader = reader;
        this.sellingReader = sellingReader;
        this.productMapper = productMapper;
        this.orderRepository = orderRepository;
        this.sellingRepository = sellingRepository;
        this.productRepository = productRepository;
    }

    // Save products
    public Mono<Result> saveProducts() {
        List<Order> orders = reader.readOrder();
        List<Receipt> receipts = sellingReader.readSoldItems();
        List<DBProduct> dbProducts = productMapper.mapProduct(orders, receipts);

        return Flux.fromIterable(dbProducts).log()
                .doOnNext(productRepository::saveProducts)
                .then(Mono.just(SUCCESS))
                .onErrorReturn(FAIL);
    }

    public Mono<Result> updateProductsByReceipts() {
        List<Receipt> receipts = sellingReader.readSoldItems();
        String itemNumber = receipts.getItems().stream().map(SoldItem::getItemNumber).findFirst().get();


        Flux.fromIterable(receipts).log().flatMapIterable(Receipt::getItems);


        fromFuture(productRepository.getProductByItemNumber(productStockData.getItemNumber()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not find product for item number")))
                .flatMap(product -> Mono.fromFuture(
                        productRepository.updateProduct(mergeDbProducts(product, dbProduct))))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not update product for item number")))
                .thenReturn(SUCCESS)
                .onErrorReturn(FAIL);
        List<DBProduct> dbProducts = productMapper.mapProduct(orders);

        return Flux.fromIterable(dbProducts).log()
                .doOnNext(productRepository::saveProducts)
                .then(Mono.just(SUCCESS))
                .onErrorReturn(FAIL);
    }

    public Mono<Result> xxxxxxxxxxx(Receipt receipt) {


        Mono.fromFuture(productRepository.getProductByItemNumber(itemNumber))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not find product for item number")))
                .flatMap(product -> Mono.fromFuture(
                        productRepository.updateProduct(mergeDbProducts(product, dbProduct))))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not update product for item number")))
                .thenReturn(SUCCESS)
                .onErrorReturn(FAIL);
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

    public Flux<ProductStockData> getAllProductStockData() {
        return Flux.from(productRepository.getAllProduct())
                .log()
                .flatMapIterable(Page::items)
                .flatMapIterable(productMapper::mapStockData);
    }

    public Flux<Product> getAllProduct() {
        return Flux.from(productRepository.getAllProduct())
                .flatMapIterable(Page::items)
                .map(product -> getProduct(product.getItemNumber()))
                .flatMap(Mono::flux);
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

    public Flux<Product> getAllSoldItemBetween(String startDate, String endDate) {

        return Flux.from(productRepository.getAllProduct())
                .flatMapIterable(Page::items)
                .flatMap(dbProduct ->
                    Flux.from(sellingRepository.getSoldItemByDate(dbProduct.getItemNumber(), startDate, endDate))
                            .map(Page::items)
                            .map(receipts -> productMapper.mapFromReceipt(dbProduct.getItemNumber(), receipts, dbProduct.getCosts()))
                ).filter(p -> p.getItemNumber() != null);
    }

    // Update the purchase price, stock information
    public Mono<Result> updateProduct(ProductStockData productStockData) {
        DBProduct dbProduct = productMapper.mapProduct(productStockData);
        return Mono.fromFuture(productRepository.getProductByItemNumber(productStockData.getItemNumber()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not find product for item number")))
                .flatMap(product -> Mono.fromFuture(
                        productRepository.updateProduct(mergeDbProducts(product, dbProduct))))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not update product for item number")))
                .thenReturn(SUCCESS)
                .onErrorReturn(FAIL);
    }

    private DBProduct mergeDbProducts(DBProduct oldProduct, DBProduct newProduct) {
        String itemNumber = oldProduct.getItemNumber();

        Integer lastSellingPrice = Optional.ofNullable(newProduct.getLastSellingPrice())
                .orElse(oldProduct.getLastSellingPrice());

        String englishName = Optional.ofNullable(newProduct.getEnglishName())
                .orElse(oldProduct.getEnglishName());

        String hungarianName = Optional.ofNullable(newProduct.getHungarianName())
                .orElse(oldProduct.getHungarianName());

        Map<String, Integer> actualStock = new HashMap<>();
        actualStock.putAll(oldProduct.getActualStock());
        for (String key: newProduct.getActualStock().keySet()) {
            Integer stock = oldProduct.getActualStock().get(key);
            Integer orderedQuantity = newProduct.getActualStock().get(key);
            int newQuantity = stock + orderedQuantity;
            actualStock.put(key, newQuantity);
        }

        Map<String, Double> costs = new HashMap<>();
        costs.putAll(oldProduct.getCosts());
        if (newProduct.getCosts() != null) {
            costs.putAll(newProduct.getCosts());
        }

        Map<String, Integer> minStock = getStockMap(oldProduct.getMinStock(), newProduct.getMinStock());
        Map<String, Integer> maxStock = getStockMap(oldProduct.getMaxStock(), newProduct.getMaxStock());

        return new DBProduct(itemNumber, englishName, hungarianName, actualStock, minStock, maxStock, costs, lastSellingPrice);
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

package com.bench.stockmanagement;

import com.bench.stockmanagement.dataaccess.Order;
import com.bench.stockmanagement.dataaccess.OrderedProduct;
import com.bench.stockmanagement.dataaccess.Receipt;
import com.bench.stockmanagement.dataaccess.SoldItem;
import com.bench.stockmanagement.domain.Product;
import com.bench.stockmanagement.services.RateExchanger;
import com.bench.stockmanagement.services.dynamo.OrderService;
import com.bench.stockmanagement.services.dynamo.SellingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.bench.stockmanagement.Constants.FORMATTER;

@Component
public class ProductHandler {
    private final RateExchanger rateExchanger;
    private final OrderService orderService;
    private final SellingService sellingService;

    @Autowired
    public ProductHandler(RateExchanger rateExchanger, OrderService orderService, SellingService sellingService) {
        this.rateExchanger = rateExchanger;
        this.orderService = orderService;
        this.sellingService = sellingService;
    }

    /**
     * From orders and selling collect the data for a Product
     *
     * Later a Product should know all selling price, and all purchase price,
     * profit also will be calculated
     *
     * The logic of creating a product is not correct, I will revise it later
     */
    public Product getProduct(String itemNumber) {
        List<Order> orders = orderService.getOrders();
        List<Receipt> allReceipt = sellingService.getAllReceipt();

        return mapProducts(orders, allReceipt, itemNumber);
    }

    private Product mapProducts(List<Order> orders,
                                List<Receipt> allReceipt, String itemNumber) {
        List<Receipt> receiptsWithItemNumber = allReceipt.stream()
                                                         .filter(receipt -> containProduct(receipt, itemNumber))
                                                         .collect(Collectors.toList());
        List<Order> ordersWithItemNumber = orders.stream()
                                                 .filter(order -> containProduct(order, itemNumber))
                                                 .collect(Collectors.toList());

        return createProduct(receiptsWithItemNumber, ordersWithItemNumber, itemNumber);
    }

    private boolean containProduct(Receipt receipt, String itemNumber) {
        return receipt.getItems()
                      .stream()
                      .anyMatch(soldItem -> soldItem.getItemNumber()
                                                    .equals(itemNumber));
    }

    private boolean containProduct(Order order, String itemNumber) {
        return order.getProducts()
                    .stream()
                    .anyMatch(orderedProduct -> orderedProduct.getItemNumber()
                                                              .equals(itemNumber));
    }

    private Product createProduct(List<Receipt> receiptsWithItemNumber, List<Order> ordersWithItemNumber,
                                  String itemNumber) {
        String englishName = null;
        String hungarianName = null;
        Integer priceToSell = null;
        Integer soldCount = 0;
        Integer orderCount = 0;
        Integer priceToBuy = 0;
        if (!receiptsWithItemNumber.isEmpty()) {
            //TODO sort it by date, get the last one
            //for szerviz ???
            Receipt aReceipt = receiptsWithItemNumber.get(0);
            soldCount = countSoldItems(receiptsWithItemNumber, itemNumber);
            Optional<SoldItem> anItem = aReceipt.getItems()
                                                .stream()
                                                .filter(soldItem -> soldItem.getItemNumber()
                                                                            .equals(itemNumber))
                                                .findFirst();

            hungarianName = anItem.map(SoldItem::getHungarianName)
                                  .orElse(null);
            priceToSell = anItem.map(SoldItem::getPrice)
                                .orElse(null);
        }

        if (!ordersWithItemNumber.isEmpty()) {
            Order anOrder = ordersWithItemNumber.get(0);
            orderCount = countOrderedItems(ordersWithItemNumber, itemNumber);

            Optional<OrderedProduct> anOrderedProduct = anOrder.getProducts()
                                                               .stream()
                                                               .filter(orderedProduct -> orderedProduct.getItemNumber()
                                                                                                       .equals(itemNumber))
                                                               .findFirst();
            englishName = anOrderedProduct.map(OrderedProduct::getEnglishName)
                                          .orElse(null);
            hungarianName = anOrderedProduct.map(OrderedProduct::getHungarianName).orElse(hungarianName);
            priceToBuy = calculatePriceToBuy(anOrder, itemNumber);
        }
        return Product.builder()
                      .itemNumber(itemNumber)
                      .englishName(englishName)
                      .hungarianName(hungarianName)
                      .priceToSell(priceToSell)
                      .priceToBuy(priceToBuy)
                      .quantity(orderCount - soldCount)
                      .ordered(orderCount)
                      .sold(soldCount)
                      .build();

    }

    private Integer countSoldItems(List<Receipt> receiptsWithItemNumber, String itemNumber) {
        int count = 0;
        for (Receipt receipt : receiptsWithItemNumber) {
            for (SoldItem item : receipt.getItems()) {
                if (item.getItemNumber()
                        .equals(itemNumber)) {
                    count += item.getQuantity();
                }
            }
        }

        return count;
    }


    private Integer countOrderedItems(List<Order> ordersWithItemNumber, String itemNumber) {
        int count = 0;
        for (Order order : ordersWithItemNumber) {
            for (OrderedProduct item : order.getProducts()) {
                if (item.getItemNumber()
                        .equals(itemNumber)) {
                    count += item.getQuantity();
                }
            }
        }

        return count;
    }

    //TODO calculate price for different orders too
    //TODO return a list of price, as from-to
    private Integer calculatePriceToBuy(Order order, String itemNumber) {
        String orderDate = order.getDate();
        LocalDate localDate = LocalDate.parse(orderDate, FORMATTER);
        int totalCount = order.getTotalProductCount();
        double shippingCost = order.getTotalShippingCost() / totalCount;
        Optional<OrderedProduct> anOrderedProduct = order.getProducts()
                                                           .stream()
                                                           .filter(orderedProduct -> orderedProduct.getItemNumber()
                                                                                                   .equals(itemNumber))
                                                           .filter(orderedProduct -> orderedProduct.getEnglishName() != null)
                                                           .findFirst();
        double price = anOrderedProduct.map(OrderedProduct::getPrice)
                                      .orElse(0.0);

        if (order.getCurrency().equals("HUF")) {
            return Double.valueOf(price + shippingCost).intValue();
        } else {
            Double usdRate = rateExchanger.getRateFor(localDate);
            return Double.valueOf(usdRate * (price + shippingCost))
                         .intValue();
        }
    }
}

package com.bench.stockmanagement.mappers;

import com.bench.stockmanagement.UnexpectedQueryResultException;
import com.bench.stockmanagement.dataaccess.DBOrder;
import com.bench.stockmanagement.domain.Order;
import com.bench.stockmanagement.domain.OrderedProduct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public List<DBOrder> mapOrder(Order order) {
        String seller = order.getSeller();
        String date = order.getDate().toString();
        Double shippingCost = order.getTotalShippingCost() / order.getTotalProductCount();
        String currency = order.getCurrency();

        List<OrderedProduct> products = order.getProducts();

        List<DBOrder> orders = new ArrayList<>();
        for (OrderedProduct product : products) {
            String itemNumber = product.getItemNumber();
            String englishName = product.getEnglishName();
            String hungarianName = product.getHungarianName();
            String attribute = product.getAttribute();
            Integer quantity = product.getQuantity();
            Double price = product.getPrice();
            String id = itemNumber + "_" + date + "_" + attribute;

            DBOrder dbOrder = new DBOrder(id, seller, date, shippingCost, currency, itemNumber,
                    englishName, hungarianName, attribute, quantity, price);
            orders.add(dbOrder);
        }
        return orders;
    }

    public List<Order> mapOrders(List<DBOrder> orders) {
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, List<DBOrder>> orderMap = new HashMap<>();

        List<String> dates = orders.stream()
                .map(DBOrder::getDate)
                .distinct()
                .collect(Collectors.toList());

        for (String date : dates) {
            List<DBOrder> list = orders.stream()
                    .filter(result -> result.getDate().equals(date))
                    .collect(Collectors.toList());
            orderMap.put(date, list);
        }

        return orderMap.values().stream().map(this::mapOrder).collect(Collectors.toList());
    }

    public Order mapOrder(List<DBOrder> orderResult) {
        if (orderResult.isEmpty()) {
            return null;
        }

        String seller = getSeller(orderResult);
        LocalDate date = getDate(orderResult);
        double totalShippingCost = getTotalShippingCost(orderResult);
        Integer totalProductCount = getTotalProductCount(orderResult);
        String currency = getCurrency(orderResult);
        List<OrderedProduct> products = getProducts(orderResult);
        return Order.builder()
                .seller(seller)
                .date(date)
                .totalShippingCost(totalShippingCost)
                .totalProductCount(totalProductCount)
                .currency(currency)
                .products(products)
                .build();
    }

    public List<OrderedProduct> getProducts(List<DBOrder> orderResult) {
        return orderResult.stream().map(result -> new OrderedProduct(result.getItemNumber(), result.getEnglishName(),
                result.getHungarianName(), result.getAttribute(), result.getQuantity(), result.getPrice())).collect(Collectors.toList());
    }

    private String getSeller(List<DBOrder> orderResult) {
        List<String> sellers = orderResult.stream().map(DBOrder::getSeller).distinct().collect(Collectors.toList());
        if (sellers.size() == 1) {
            return sellers.get(0);
        } else {
            throw new UnexpectedQueryResultException("Unexpected seller returned! Result=" + sellers);
        }
    }

    private LocalDate getDate(List<DBOrder> orderResult) {
        List<String> orderDates = orderResult.stream().map(DBOrder::getDate).distinct().collect(Collectors.toList());
        if (orderDates.size() == 1) {
            return LocalDate.parse(orderDates.get(0));
        } else {
            throw new UnexpectedQueryResultException("Unexpected order date returned! Result=" + orderDates);
        }
    }

    private double getTotalShippingCost(List<DBOrder> orderResult) {
        double totalShippingCost= 0;
        for (DBOrder order : orderResult) {
            totalShippingCost += order.getShippingCost() * order.getQuantity();
        }
        return totalShippingCost;
    }

    private Integer getTotalProductCount(List<DBOrder> orderResult) {
        int totalProductionCount= 0;
        for (DBOrder order : orderResult) {
            totalProductionCount += order.getQuantity();

        }
        return totalProductionCount;
    }

    private String getCurrency(List<DBOrder> orderResult) {
        List<String> currencies = orderResult.stream().map(DBOrder::getCurrency).distinct().collect(Collectors.toList());
        if (currencies.size() == 1) {
            return currencies.get(0);
        } else {
            throw new UnexpectedQueryResultException("Unexpected currency returned! Result=" + currencies);
        }
    }
}

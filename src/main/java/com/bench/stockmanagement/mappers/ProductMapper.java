package com.bench.stockmanagement.mappers;

import com.amazonaws.util.StringUtils;
import com.bench.stockmanagement.dataaccess.*;
import com.bench.stockmanagement.domain.*;
import com.bench.stockmanagement.services.RateExchanger;
import io.netty.util.internal.StringUtil;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.utils.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    private final RateExchanger rateExchanger;

    public ProductMapper(RateExchanger rateExchanger) {
        this.rateExchanger = rateExchanger;
    }

    public List<DBExtendedProduct> mapProduct(List<Order> orders, List<Receipt> receipts) {
        List<DBExtendedProduct> dbProducts = new ArrayList<>();

        List<String> itemNumbers = orders.stream()
                .map(Order::getProducts)
                .flatMap(List::stream)
                .map(OrderedProduct::getItemNumber)
                .distinct()
                .collect(Collectors.toList());

        for (String in : itemNumbers) {
            List<OrderDetails> orderDetails = mapOrderDetails(in, orders);
            List<SellingDetails> sellingDetails = mapSellingDetails(in, receipts);

            Map<String, Integer> stockMap = orderDetails.stream().map(OrderDetails::getAttribute).collect(Collectors.toMap(
                    a -> StringUtil.isNullOrEmpty(a) ? "all" : a, x -> 0));

            dbProducts.add(new DBExtendedProduct(in, stockMap, stockMap, orderDetails, sellingDetails));
        }

        return dbProducts;
    }

    private List<SellingDetails> mapSellingDetails(String itemNumber, List<Receipt> receipts) {
        List<Receipt> receiptsWithItemNumber = receipts.stream()
                    .filter(r ->r.getItems()
                            .stream()
                            .anyMatch(soldItem -> soldItem.getItemNumber().equals(itemNumber)))
                    .collect(Collectors.toList());


        List<String> attributes = receipts.stream()
                .map(Receipt::getItems)
                .flatMap(List::stream)
                .filter(item -> item.getItemNumber().equals(itemNumber))
                .map(SoldItem::getAttribute)
                .distinct()
                .collect(Collectors.toList());

        for (String attribute : attributes) {
            String hungarianName = receipts.stream()
                    .map(Receipt::getItems)
                    .flatMap(List::stream)
                    .filter(item -> item.getAttribute().equals(attribute))
                    .map(SoldItem::getHungarianName)
                    .distinct()
                    .findFirst()
                    .orElse("");

            Map<String, Integer> selling = receipts.stream()
                    .map(Receipt::getItems)
                    .flatMap(List::stream)
                    .filter(item -> item.getAttribute().equals(attribute))
                    .collect(Collectors.toMap(SoldItem::getItemNumber, SoldItem::getQuantity));

            new SellingDetails(hungarianName, attribute, selling);
        }
//
//            Map<String, Double> costMap = ordersWithItemNumber.stream()
//                    .collect(Collectors.toMap(o -> o.getDate().toString(), o -> mapCost(o, in)));


        return null;
    }

    private List<OrderDetails> mapOrderDetails(String itemNumber, List<Order> orders) {
//        String englishName = ordersWithItemNumber.stream()
//                    .map(Order::getProducts)
//                    .flatMap(List::stream)
//                    .filter(orderedProduct -> orderedProduct.getItemNumber().equals(in))
//                    .map(OrderedProduct::getEnglishName)
//                    .distinct()
//                    .findFirst()
//                    .orElse("");
//
//        Map<String, Double> costMap = ordersWithItemNumber.stream()
//                    .collect(Collectors.toMap(o -> o.getDate().toString(), o -> mapCost(o, in)));

        return null;
    }

//    public List<DBProduct> mapProduct(List<Order> orders, List<Receipt> receipts) {
//        List<DBProduct> dbProducts = new ArrayList<>();
//
//        List<String> itemNumbers = orders.stream()
//                .map(Order::getProducts)
//                .flatMap(List::stream)
//                .map(OrderedProduct::getItemNumber)
//                .distinct()
//                .collect(Collectors.toList());
//
//        List<OrderDetails> orderDetails = mapOrderDetails(itemNumbers, orders);
//        List<SellingDetails> sellingDetails = mapSellingDetails(itemNumbers, receipts);
//
//
//        for (String in : itemNumbers) {
//            List<Order> ordersWithItemNumber = orders.stream()
//                    .filter(o -> o.getProducts()
//                            .stream()
//                            .anyMatch(orderedProduct -> orderedProduct.getItemNumber().equals(in)))
//                    .collect(Collectors.toList());
//
//            List<String> attributes = ordersWithItemNumber.stream()
//                    .map(Order::getProducts)
//                    .flatMap(List::stream)
//                    .filter(orderedProduct -> orderedProduct.getItemNumber().equals(in))
//                    .map(OrderedProduct::getAttribute)
//                    .distinct()
//                    .collect(Collectors.toList());
//
//            String englishName = ordersWithItemNumber.stream()
//                    .map(Order::getProducts)
//                    .flatMap(List::stream)
//                    .filter(orderedProduct -> orderedProduct.getItemNumber().equals(in))
//                    .map(OrderedProduct::getEnglishName)
//                    .distinct()
//                    .findFirst()
//                    .orElse("");
//
//            String hungarianName = ordersWithItemNumber.stream()
//                    .map(Order::getProducts)
//                    .flatMap(List::stream)
//                    .filter(orderedProduct -> orderedProduct.getItemNumber().equals(in))
//                    .map(OrderedProduct::getHungarianName)
//                    .distinct()
//                    .findFirst()
//                    .orElse("");
//
//            Map<String, Double> costMap = ordersWithItemNumber.stream()
//                    .collect(Collectors.toMap(o -> o.getDate().toString(), o -> mapCost(o, in)));
//            Map<String, Integer> stockMap = attributes.stream().collect(Collectors.toMap(
//                    a -> StringUtil.isNullOrEmpty(a) ? "all" : a, x -> 0));
//
//            Map<String, Integer> actualStock = mapActualStock(ordersWithItemNumber, attributes, in);
//
//            dbProducts.add(new DBProduct(in, englishName, hungarianName, actualStock, stockMap, stockMap, costMap, null));
//        }
//        return dbProducts;
//    }
//
//    private static Map<String, Integer> mapActualStock(List<Order> ordersWithItemNumber, List<String> attributes, String in) {
//        List<OrderedProduct> orderedProducts = ordersWithItemNumber.stream()
//                .map(Order::getProducts)
//                .flatMap(List::stream)
//                .filter(orderedProduct -> orderedProduct.getItemNumber().equals(in)).collect(Collectors.toList());
//
//        Map<String, Integer> actualStock = new HashMap<>();
//        if (attributes.isEmpty()) {
//            Integer allOrderedQuantity = orderedProducts.stream()
//                    .map(OrderedProduct::getQuantity)
//                    .reduce(0, Integer::sum);
//            actualStock.put("all", allOrderedQuantity);
//        } else {
//            for (String attribute : attributes) {
//                Integer allOrderedQuantity = orderedProducts.stream()
//                        .filter(product -> product.getAttribute().equals(attribute))
//                        .map(OrderedProduct::getQuantity)
//                        .reduce(0, Integer::sum);
//                actualStock.put(attribute, allOrderedQuantity);
//            }
//        }
//
//        return actualStock;
//    }
//
//    public DBProduct mapProduct(ProductStockData productStockData) {
////        String itemNumber = productStockData.getItemNumber();
////        Integer lastSellingPrice = productStockData.getActualPrice();
////        String attribute = productStockData.getAttribute();
////        Map<String, Integer> minStock = getStockMap(attribute, productStockData.getMinStock());
////        Map<String, Integer> maxStock = getStockMap(attribute, productStockData.getMaxStock());
////        Map<String, Double> costs = getDbProductCosts(productStockData);
////        return new DBProduct(itemNumber, lastSellingPrice, minStock, maxStock, costs);
//        return new DBProduct();
//    }
//
//    public List<ProductStockData> mapStockData(DBProduct product) {
//        String itemNumber = product.getItemNumber();
//        Integer lastSellingPrice = product.getLastSellingPrice();
//        Map<String, Integer> minStockMap = product.getMinStock();
//        Map<String, Integer> maxStockMap = product.getMaxStock();
//
//        Pair<LocalDate, Double> purchaseCost = mapPurchaseCost(product.getCosts());
//        List<ProductStockData> result = new ArrayList<>();
//
//        for (String key : minStockMap.keySet()) {
//            int maxStock = maxStockMap.get(key);
//            int minStock = minStockMap.get(key);
//
//            ProductStockData productStockData = ProductStockData.builder()
//                    .itemNumber(itemNumber)
//                    .attribute(key)
//                    .minStock(minStock)
//                    .maxStock(maxStock)
//                    .actualPrice(lastSellingPrice)
//                    .lastPurchaseCost(purchaseCost.right())
//                    .lastPurchaseDate(purchaseCost.left())
//                    .build();
//            result.add(productStockData);
//        }
//        return result;
//    }
//
//    public Product map(String itemNumber, List<DBOrder> orderedProducts, List<DBReceipt> soldItems) {
//        String englishName = getEnglishName(orderedProducts);
//        String hungarianName = getHungarianName(orderedProducts, soldItems);
//        List<PurchaseInformation> purchaseInformationList = getPurchaseInformation(orderedProducts);
//        List<SellingInformation> sellingInformationList = getSellingInformation(soldItems);
//        int quantity = calculateQuantity(purchaseInformationList, sellingInformationList);
//
//        int actualProfit = calculateActualProfit(purchaseInformationList, sellingInformationList);
//
//        return new Product(itemNumber, englishName, hungarianName, quantity, sellingInformationList,
//                purchaseInformationList, actualProfit);
//    }
//
//    private Double mapCost(Order order, String in) {
//        double shippingCost = order.getTotalShippingCost() / order.getTotalProductCount();
//        Double price = order.getProducts()
//                .stream()
//                .filter(op -> op.getItemNumber().equals(in)).findFirst().get().getPrice();
//        return shippingCost + price;
//    }
//
//    private Map<String, Double> getDbProductCosts(ProductStockData productStockData) {
//        String date = Optional.ofNullable(productStockData.getLastPurchaseDate()).map(LocalDate::toString).orElse(null);
//        Double purchaseCost = productStockData.getLastPurchaseCost();
//        if (date == null && purchaseCost == null) {
//            return null;
//        }
//        return Collections.singletonMap(date, purchaseCost);
//    }
//
//    private Map<String, Integer> getStockMap(String attribute, Integer stockValue) {
//        if (StringUtils.isNullOrEmpty(attribute) && stockValue == null) {
//            return null;
//        }
//        String attr = StringUtils.isNullOrEmpty(attribute) ? "all" : attribute;
//        return Map.of(attr, stockValue);
//    }
//
//    private Pair<LocalDate, Double> mapPurchaseCost(Map<String, Double> dbProductCost) {
//        List<String> sortedKeys = new ArrayList<>(dbProductCost.keySet());
//        sortedKeys.sort(Comparator.reverseOrder());
//
//        String lastPurchaseDate = sortedKeys.get(0);
//        LocalDate date = LocalDate.parse(lastPurchaseDate);
//
//        Double usdRate = rateExchanger.getRateFor(date);
//
//        double purchaseCost = dbProductCost.get(lastPurchaseDate) * usdRate;
//        return Pair.of(date, purchaseCost);
//    }
//
//    private int calculateActualProfit(List<PurchaseInformation> purchaseInformationList, List<SellingInformation> sellingInformationList) {
//        purchaseInformationList.sort(Comparator.comparing(PurchaseInformation::getOrderDate).reversed());
//        sellingInformationList.sort(Comparator.comparing(SellingInformation::getSellingDate).reversed());
//
//        int actualSellingPrice = sellingInformationList.stream()
//                .findFirst()
//                .map(SellingInformation::getPrice)
//                .orElse(0);
//        int actualPurchasedPrice = purchaseInformationList.stream()
//                .findFirst()
//                .map(PurchaseInformation::getPurchasePrice)
//                .orElse(0);
//        return actualSellingPrice - actualPurchasedPrice;
//    }
//
//    private String getHungarianName(List<DBOrder> orderedProducts, List<DBReceipt> soldItems) {
//        String hungarianName = orderedProducts.stream()
//                .map(DBOrder::getHungarianName)
//                .filter(Objects::nonNull)
//                .findFirst()
//                .orElse(null);
//        return soldItems.stream().map(DBReceipt::getHungarianName).findFirst().orElse(hungarianName);
//    }
//
//    private String getEnglishName(List<DBOrder> orderedProducts) {
//        return orderedProducts.stream()
//                .map(DBOrder::getEnglishName)
//                .filter(Objects::nonNull)
//                .findFirst()
//                .orElse(null);
//    }
//
//    private Integer calculateQuantity(List<PurchaseInformation> purchaseInformationList, List<SellingInformation> sellingInformationList) {
//        int purchasedQuantity = purchaseInformationList.stream().map(PurchaseInformation::getQuantity).reduce(
//                Integer::sum).orElse(0);
//        int soldQuantity = sellingInformationList.stream()
//                .map(SellingInformation::getQuantity)
//                .reduce(Integer::sum)
//                .orElse(0);
//        return purchasedQuantity - soldQuantity;
//    }
//
//    private List<SellingInformation> getSellingInformation(List<DBReceipt> soldItems) {
//        List<SellingInformation> sellingInformationList = new ArrayList<>();
//
//        for (DBReceipt receipt : soldItems) {
//            LocalDate orderDate = LocalDate.parse(receipt.getDate());
//            int quantity = receipt.getQuantity();
//            int price = receipt.getPrice();
//            String attribute = receipt.getAttribute();
//
//            SellingInformation sellingInformation = new SellingInformation(orderDate, quantity, price, attribute);
//
//            sellingInformationList.add(sellingInformation);
//        }
//        return sellingInformationList;
//    }
//
//    private List<PurchaseInformation> getPurchaseInformation(List<DBOrder> orderedProducts) {
//        List<PurchaseInformation> purchaseInformationList = new ArrayList<>();
//
//        for (DBOrder order : orderedProducts) {
//            LocalDate orderDate = LocalDate.parse(order.getDate());
//            String currency = order.getCurrency();
//            double rate = getRate(currency, orderDate);
//            int quantity = order.getQuantity();
//            int productPrice = (int) Math.round(order.getPrice() * rate);
//            int shippingCost = (int) Math.round(order.getShippingCost() * rate);
//            int purchasePrice = productPrice + shippingCost;
//            String attribute = order.getAttribute();
//
//            PurchaseInformation purchaseInformation = new PurchaseInformation(orderDate, quantity, productPrice,
//                    shippingCost, purchasePrice, attribute);
//
//            purchaseInformationList.add(purchaseInformation);
//        }
//        return purchaseInformationList;
//    }
//
//    private double getRate(String currency, LocalDate orderDate) {
//        if ("HUF".equals(currency)) {
//            return 1;
//        }
//        return rateExchanger.getRateFor(orderDate);
//    }
//
//    public Product mapFromReceipt(String itemNumber, List<DBReceipt> dbReceipts, Map<String, Double> costs) {
//        if (dbReceipts.isEmpty()) {
//            return Product.builder().build();
//        }
//
//        String englishName = null;
//        String hungarianName = getHungarianName(Collections.emptyList(), dbReceipts);
//        List<PurchaseInformation> purchaseInformationList = Collections.emptyList();
//        List<SellingInformation> sellingInformationList = getSellingInformation(dbReceipts);
//        int quantity = 0;
//
//        sellingInformationList.sort(Comparator.comparing(SellingInformation::getSellingDate).reversed());
//        List<String> sorted = costs.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
//        String lastOrderDate = sorted.get(0);
//        Double rateFor = rateExchanger.getRateFor(LocalDate.parse(lastOrderDate));
//        Double priceInDollar = costs.get(lastOrderDate);
//        int actualProfit = Double.valueOf(sellingInformationList.get(0).getPrice() - (priceInDollar * rateFor)).intValue();
//
//        return new Product(itemNumber, englishName, hungarianName, quantity, sellingInformationList,
//                purchaseInformationList, actualProfit);
//    }
}

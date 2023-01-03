package com.bench.stockmanagement

import com.bench.stockmanagement.kotlin.model.*
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun main(args: Array<String>) {
    var orderFileName0 = "./src/main/resources/adasveteli.csv"
    var orderFileName1 = "./src/main/resources/Alice-2022-01-10.csv"
    var orderFileName2 = "./src/main/resources/Alice-2022-02-10.csv"
    var orderFileName3 = "./src/main/resources/Alice-2022-03-04.csv"
    var orderFileName4 = "./src/main/resources/Alice-2022-04-25.csv"
    var orderFileName5 = "./src/main/resources/Alice-2022-05-23.csv"
    var orderFileName6 = "./src/main/resources/Alice-2022-06-01.csv"
    var orderFileName7 = "./src/main/resources/Alice-2022-06-13.csv"
    var orderFileName8 = "./src/main/resources/Alice-2022-07-06.csv"
    var orderFileName9 = "./src/main/resources/Alice-2022-07-18.csv"
    var orderFileName10 = "./src/main/resources/Alice-2022-07-22.csv"
    var orderFileName11 = "./src/main/resources/Alice_2022_08_05.csv"
    var orderFileName12 = "./src/main/resources/Alice_2022_09_08.csv"
    var orderFileName13 = "./src/main/resources/Alice_2022_10_13.csv"
    var orderFileName14 = "./src/main/resources/Alice_2022_10_17.csv"
    var orderFileName15 = "./src/main/resources/Alice_2022_11_15.csv"
    var orderFileName16 = "./src/main/resources/Lizzy-2022-06-27.csv"
    var orderFileName17 = "./src/main/resources/Lizzy-2022-06-29.csv"
    var orderFileName18 = "./src/main/resources/Monorim-2022-08-03.csv"
    var orderFileName19 = "./src/main/resources/Pxid-2022-03-30.csv"
    var orderFileName20 = "./src/main/resources/Pxid-2022-06-17.csv"
    var orderFileName21 = "./src/main/resources/Pxid-2022-09-23.csv"

    var orderList = mutableListOf(orderFileName0, orderFileName1, orderFileName2, orderFileName3, orderFileName4,
            orderFileName5, orderFileName6, orderFileName7, orderFileName8, orderFileName9, orderFileName10,
            orderFileName11, orderFileName12, orderFileName13, orderFileName14, orderFileName15, orderFileName16,
            orderFileName17, orderFileName18, orderFileName19, orderFileName20, orderFileName21)

    var receiptsFileName1 = "./src/main/resources/2022_01_01-02_28.txt"
    var receiptsFileName3 = "./src/main/resources/2022_03_01-03_31.txt"
    var receiptsFileName4 = "./src/main/resources/2022_04_01-04_30.txt"
    var receiptsFileName5 = "./src/main/resources/2022_05_01-05_31.txt"
    var receiptsFileName6 = "./src/main/resources/2022_06_01-06_30.txt"
    var receiptsFileName7 = "./src/main/resources/2022_07_01-07_31.txt"
    var receiptsFileName8 = "./src/main/resources/2022_08_01-08_31.txt"
    var receiptsFileName9 = "./src/main/resources/2022_09_01-09_30.txt"
    var receiptsFileName10 = "./src/main/resources/2022_10_01-10_31.txt"
    var receiptsFileName11 = "./src/main/resources/2022_11_01-11_30.txt"
    var receiptsFileName12 = "./src/main/resources/2022_12_01_12_31.txt"

    var receiptList = mutableListOf(receiptsFileName1, receiptsFileName3, receiptsFileName4, receiptsFileName5,
            receiptsFileName6, receiptsFileName7, receiptsFileName8, receiptsFileName9, receiptsFileName10,
            receiptsFileName11, receiptsFileName12)

    var products = OrderedProducts(mutableSetOf())
    val orders = orderList.map { readOrders(it) }
    for (l in orders) {
        products = sumOrders(l, products)
    }
    println(products)

    var sProducts = SoldProducts(mutableSetOf())
    val sellings = receiptList.map { readReceipts(it) }
    for (s in sellings) {
        sProducts = sumSelling(s, sProducts)
    }
    println(sProducts)

    var allProduct = mergeProducts(products, sProducts)
//    println(allProduct)
    writeToFile(allProduct)
}

fun writeToFile(list: List<Product>) {
//    val path = Paths.get("foo.out")
//    val text = "Some log…"

    val path = Paths.get("./src/main/resources/export.csv")
    Files.newBufferedWriter(path, charset("UTF_16LE")).use {
         out ->

            out.write("Cikkszám,Angol név,Magyar név,Tulajdonság,Összes rendelés,Összes eladás,Készleten")
            out.newLine()
            for (l in list) {
                out.write(l.asLine())
                out.newLine()
            }
        }

        println("Writed to file")

}
fun mergeProducts(orderedProducts: OrderedProducts, soldProducts: SoldProducts): List<Product> {
    val ordered = orderedProducts.products
    val sold = soldProducts.products

    var allProductsList: List<Product> = mutableListOf()
    for (o in ordered) {
        val itemNumber = o.itemNumber
        val englishName = o.englishName
        val attribute = o.attribute
        var totalOrder = o.orderedQuantity
        val hungarianName = sold.find { soldProduct -> soldProduct.itemNumber.equals(itemNumber) && soldProduct.attribute == attribute }?.hungarianName ?: ""
        val totalSold = sold.find { soldProduct -> soldProduct.itemNumber.equals(itemNumber) && soldProduct.attribute == attribute }?.soldQuantity ?: 0
        val inStock = totalOrder - totalSold

        var product = Product(itemNumber, englishName, hungarianName, totalOrder, totalSold, inStock, attribute)
        allProductsList = allProductsList.plus(product)
    }

    for (o in sold) {
        val itemNumber = o.itemNumber
        val attribute = o.attribute

        if (allProductsList.firstOrNull{product: Product -> product.itemNumber.equals(itemNumber) && product.atribute == attribute} == null) {
            val totalOrder = 0
            val hungarianName =
                sold.find { soldProduct -> soldProduct.itemNumber.equals(itemNumber) && soldProduct.attribute == attribute }?.hungarianName ?: ""
            val totalSold =
                sold.find { soldProduct -> soldProduct.itemNumber.equals(itemNumber) && soldProduct.attribute == attribute }?.soldQuantity ?: 0
            val inStock = totalOrder - totalSold

            var product = Product(itemNumber, "", hungarianName, totalOrder, totalSold, inStock, attribute)
            allProductsList = allProductsList.plus(product)
        }
    }
    val comparator = Comparator { p1: Product, p2: Product -> p1.itemNumber.compareTo(p2.itemNumber)}
    allProductsList = allProductsList.sortedWith(comparator)
    return allProductsList
}

data class Product(var itemNumber: String,
                   var englishName: String,
                   var hungarianName: String,
                   var ordered: Int,
                   var sold: Int,
                   var stock: Int,
                   var atribute: Attribute?) {
    fun asLine(): String {
        return "$itemNumber,$englishName,${hungarianName.replace(",", " ")},${atribute!!.hungarian},$ordered,$sold,$stock"
    }
}

data class OrderedProducts(var products: Set<OrderedProduct>) {
    fun add(product: OrderedProduct) {
        products.plus(product)
    }
    fun increaseQuantity(itemNumber: String, quantity: Int) {
        this.products.find { it.itemNumber.equals(itemNumber) }?.orderedQuantity?.plus(quantity)
    }
}

data class SoldProducts(var products: Set<SoldProduct>) {
    fun add(product: OrderedProduct) {
        products.plus(product)
    }
}

fun sumOrders(orderedItems: List<OrderedItem?>, products: OrderedProducts):OrderedProducts {
    var allProducts = products.products
    for (item: OrderedItem? in orderedItems)
    {
        if (item == null) { break }
        val orderedProduct = OrderedProduct(item.itemNumber, item.englishItemName, item.quantity, item.attribute)
        val oldProductValue = allProducts.filter { it.equalsByItemNumber(orderedProduct) }.firstOrNull()
        if (oldProductValue ==null) {
            allProducts = allProducts.plus(orderedProduct)
        } else {
            val increasedQuantity = oldProductValue.increaseQuantity(orderedProduct.orderedQuantity)
            allProducts = allProducts.plus(increasedQuantity)
        }
    }
    val comparator = Comparator { p1: OrderedProduct, p2: OrderedProduct -> p1.itemNumber.compareTo(p2.itemNumber)}
    allProducts = allProducts.sortedWith(comparator).toSet()
    return OrderedProducts(allProducts)
}


fun sumSelling(soldItems: List<SoldItem?>, products: SoldProducts):SoldProducts {
    var allProducts = products.products
    for (item: SoldItem? in soldItems)
    {
        if (item == null) { break }
        val soldProduct = SoldProduct(item.kod, item.termek, item.mennyiseg, item.tulajdonsag)
        val oldProductValue = allProducts.filter { it.equalsByItemNumber(soldProduct) }.firstOrNull()
        if (oldProductValue ==null) {
            allProducts = allProducts.plus(soldProduct)
        } else {
            val increasedQuantity = oldProductValue.increaseQuantity(soldProduct.soldQuantity)
            allProducts = allProducts.plus(increasedQuantity)
        }
    }
    val comparator = Comparator { p1: SoldProduct, p2: SoldProduct -> p1.itemNumber.compareTo(p2.itemNumber)}
    allProducts = allProducts.sortedWith(comparator).toSet()
    return SoldProducts(allProducts)
}


fun readOrders(fileName: String): List<OrderedItem?> {
//    println(fileName)
    var lines = File(fileName).readLines()
    val firstLineParts = lines.first().split(",")
    var sellerName = firstLineParts[0].filter { c -> c.isLetter() }
    var orderDate = firstLineParts[1].filter { c -> c.isDigit() || c.equals('-') }
    var totalShippingCost = BigDecimal.valueOf(firstLineParts[2].filter { c -> c.isDigit() || c.equals('.') }.toDouble())
    var totalQuantity = BigDecimal.valueOf(firstLineParts[3].filter { c -> c.isDigit() }.toDouble())
    var shippingCost = totalShippingCost / totalQuantity
    lines = lines.subList(1, lines.size)
    return lines.map { mapOrder(it, sellerName, orderDate, shippingCost) }.filter { Objects.nonNull(it) }
}

fun mapOrder(line: String, sellerName: String, orderDate: String, shippingCost: BigDecimal): OrderedItem? {
    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    println(line)
    var parts = line.split(",")
    if (parts.size == 1) {
        return null
    }
    var itemNumber = parts[0].filter { c -> c.isLetterOrDigit() || c.equals('-') }.uppercase()
    var englishItemName = parts[1]
    var hungarianItemName = parts[2]
    var attribute = Attribute.english(parts[3].filter { c -> c.isLetterOrDigit() || c.equals('-') })
    var quantity = parts[4].filter { c -> c.isDigit() }.toInt()
    var price = BigDecimal.valueOf(parts[5].filter { c -> c.isDigit() || c.equals('.') }.toDouble())

    return OrderedItem(itemNumber, englishItemName, hungarianItemName, attribute, quantity, price, shippingCost, orderDate, sellerName)
}


// M-8C,Silicone cover for throttle,,Black,2,1.3


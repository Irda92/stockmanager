package com.bench.stockmanagement.kotlin.service

import com.bench.stockmanagement.kotlin.model.OrderedItem
import com.bench.stockmanagement.kotlin.model.SoldItem
import com.bench.stockmanagement.readOrders
import com.bench.stockmanagement.readReceipts
import com.bench.stockmanagement.services.RateExchanger
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

fun main(args: Array<String>) {
    var receiptsFileName = "D:/Developing/workplace/stockmanager/src/main/resources/2022_01_01-02_28.txt"
    var szamlak = readReceipts(receiptsFileName)

    var orderFileName = "D:/Developing/workplace/stockmanager/src/main/resources/Alice-2022-01-10.csv"

    var order = readOrders(orderFileName)

    var totalProfit = 0
    var gls = 0
    var profitPerItems = mutableMapOf<String, Int>()// HashMap<String, Int>()
    szamlak.forEach {
        val profit = calculateProfitPerItems(order, it)
        if (it!!.kod.isEmpty()) gls += profit
        else {
//            if (profitPerItems.get(it.kod) != null) {
//                var element = profitPerItems.get(it.kod)
//                var newValue = element!!.plus(profit)
//                profitPerItems.put(it.kod, newValue)
//            } else
            profitPerItems.put(it.kod, profit)

            totalProfit += profit
        }
//        println("${it!!.termek} - ${it.kod} : $profit")
//        if (it.kod.isNotEmpty()) totalProfit += profit else gls += profit
    }
    profitPerItems.forEach { println("${it.key}: ${it.value}") }
    println("Összes profit: $totalProfit")
    println("Összes GLS díj: $gls")
}

fun calculateProfitPerItems(orders: List<OrderedItem?>, soldItem: SoldItem?): Int {
    if (soldItem == null) return 0

    val actualOrderedItem = orders.requireNoNulls().find { orderedItem -> orderedItem.itemNumber.equals(soldItem.kod) }

    if (actualOrderedItem == null) return soldItem.ar * soldItem.mennyiseg

    val price = actualOrderedItem.price
    val shippingCost = actualOrderedItem.shippingCost

    val orderDate = LocalDate.parse(actualOrderedItem.orderDate)
    val usdRate = BigDecimal.valueOf(RateExchanger.getRateFor(orderDate))

    val totalCost = (price * usdRate) + (shippingCost * usdRate)

    return (soldItem.ar.minus(totalCost.toInt())) * soldItem.mennyiseg
}

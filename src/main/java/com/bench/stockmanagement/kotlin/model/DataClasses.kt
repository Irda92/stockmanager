package com.bench.stockmanagement.kotlin.model

import java.math.BigDecimal

data class OrderedItem(var itemNumber: String,
                       var englishItemName: String,
                       var hungarianItemName: String,
                       var attribute: Attribute,
                       var quantity: Int,
                       var price: BigDecimal,
                       var shippingCost: BigDecimal,
                       var orderDate: String,
                       var sellerName: String
)

data class SoldItem(var sorszam: String,
                    var datum: String,
                    var termek: String,
                    var kod: String,
                    var tulajdonsag: Attribute,
                    var mennyiseg: Int,
                    var ar: Int,
                    var fizetesiMod: String
)

data class BankiTranzakcio(var szamlaszam: String,
                           var tipus: String,
                           var osszeg: Int,
                           var penznem: String,
                           var konyvelesDatum: String,
                           var tranzakcioDatum: String,
                           var egyenleg: Int,
//                           var masikSzamlaszam: String,
                           var kedvezmenyezett: String,
                           var kozlemeny: String,
                           var megjegyzes: String
)

data class SoldProduct(var itemNumber: String,
                       var hungarianName: String,
                       var soldQuantity: Int,
                       var attribute: Attribute) {
    fun increaseQuantity(quantity: Int):SoldProduct {
        this.soldQuantity = this.soldQuantity.plus(quantity)
        return this
    }
    fun equalsByName(other: SoldProduct): Boolean {
        return this.itemNumber.equals(other.itemNumber) &&
                this.hungarianName.equals(other.hungarianName) &&
                this.attribute.equals(other.attribute)
    }
    fun equalsByItemNumber(other: SoldProduct): Boolean {
        return this.itemNumber.equals(other.itemNumber) &&
                this.attribute.equals(other.attribute)
    }
}

data class OrderedProduct(var itemNumber: String,
                          var englishName: String,
                          var orderedQuantity: Int,
                          var attribute: Attribute) {
    fun increaseQuantity(quantity: Int):OrderedProduct {
        this.orderedQuantity = this.orderedQuantity.plus(quantity)
        return this
    }
    fun equalsByName(other: OrderedProduct): Boolean {
        return this.itemNumber.equals(other.itemNumber) &&
                this.englishName.equals(other.englishName) &&
                this.attribute.equals(other.attribute)
    }
    fun equalsByItemNumber(other: OrderedProduct): Boolean {
        return this.itemNumber.equals(other.itemNumber) &&
                this.attribute.equals(other.attribute)
    }
}

enum class Attribute(var english: String,
                     var hungarian: String) {

    BLACK("Black", "Fekete"),
    WHITE("White", "Fehér"),
    YELLOW("Yellow", "Sárga"),
    GREEN("Green", "Zöld"),
    BLUE("Blue", "Kék");

    public fun english(value: String): Attribute {
        return valueOf(value)
    }

    infix fun hungarian(value: String): Attribute? {
        return values().find { attribute -> attribute.hungarian == value }
    }
}
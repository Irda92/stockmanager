package com.bench.stockmanagement.kotlin.model

import java.math.BigDecimal

data class OrderedItem(var itemNumber: String,
                       var englishItemName: String,
                       var hungarianItemName: String,
                       var attribute: Attribute?,
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
                    var tulajdonsag: Attribute?,
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
                       var attribute: Attribute?) {
    fun increaseQuantity(quantity: Int):SoldProduct {
        this.soldQuantity = this.soldQuantity.plus(quantity)
        return this
    }
    fun equalsByName(other: SoldProduct): Boolean {
        return this.itemNumber.equals(other.itemNumber) &&
                this.hungarianName.equals(other.hungarianName) &&
                this.attribute!!.equals(other.attribute)
    }
    fun equalsByItemNumber(other: SoldProduct): Boolean {
        return this.itemNumber.equals(other.itemNumber) &&
                this.attribute!!.equals(other.attribute)
    }
}

data class OrderedProduct(var itemNumber: String,
                          var englishName: String,
                          var orderedQuantity: Int,
                          var attribute: Attribute?) {
    fun increaseQuantity(quantity: Int):OrderedProduct {
        this.orderedQuantity = this.orderedQuantity.plus(quantity)
        return this
    }
    fun equalsByName(other: OrderedProduct): Boolean {
        return this.itemNumber.equals(other.itemNumber) &&
                this.englishName.equals(other.englishName) &&
                this.attribute!!.equals(other.attribute)
    }
    fun equalsByItemNumber(other: OrderedProduct): Boolean {
        return this.itemNumber.equals(other.itemNumber) &&
                this.attribute!!.equals(other.attribute)
    }
}

enum class Attribute(var english: String,
                     var hungarian: String) {

    BLACK("Black", "Fekete"),
    WHITE("White", "Fehér"),
    YELLOW("Yellow", "Sárga"),
    GREEN("Green", "Zöld"),
    BLUE("Blue", "Kék"),
    DARK_BLUE("Dark blue", "Sötét kék"),
    RED("Red", "Piros"),
    ORANGE("Orange", "Narancssárga"),
    SILVER("Silver", "Ezüst"),
    GREY("Grey", "Szürke"),
    PURPLE("Purple", "Lila"),
    PINK("Pink", "Rózsaszín"),
    GOLD("Gold", "Arany"),

    COLORFUL("Colourful", "Színes"),
    SUPREME("Supreme", "Supreme"),
    BATMAN("Batman", "Batman"),

    BLACK_20_MM("Black20mm", "Fekete 20 mm"),
    BLACK_10_MM("Black10mm", "Fekete 10 mm"),
    BLUE_10_MM("Blue10mm", "Kék 10 mm"),

    M("M", "M"),
    L("L", "L"),
    XL("XL", "XL"),
    XXL("XXL", "XXL"),

    DEGREE_45("45degree", "45fokos"),
    DEGREE_90("90degree", "90fokos"),
    DEGREE_0("0degree", "0fokos"),

    ONE("1", "1"),
    TWO("2", "2"),
    THREE("3", "3"),
    FOUR("4", "4"),
    FIVE("5", "5"),
    SIX("6", "6"),
    SEVEN("7", "7"),
    EIGHT("8", "8"),
    NINE("9", "9"),
    TEN("10", "10"),
    ELEVEN("11", "11"),
    TWELVE("12", "12"),

    NOTHING("", "");

    companion object {
        fun english(value: String): Attribute? {
            val firstOrNull = values().firstOrNull { attribute -> attribute.english == value }
            if (firstOrNull == null) println("$value - $firstOrNull")
            return firstOrNull
        }

        fun hungarian(value: String): Attribute? {
            val firstOrNull = values().firstOrNull { attribute -> attribute.hungarian == value }
            if (firstOrNull == null) println("$value - $firstOrNull")
            return firstOrNull
        }
    }
}
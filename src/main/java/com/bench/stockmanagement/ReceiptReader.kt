package com.bench.stockmanagement

import com.bench.stockmanagement.kotlin.model.Attribute
import com.bench.stockmanagement.kotlin.model.SoldItem
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    var fileNameAprilis = "D:/Developing/workplace/stockmanager/src/main/resources/2022_04_01-04_30.txt"
    var fileNameAugusztus = "D:/Developing/workplace/stockmanager/src/main/resources/2022_08_01-08_31.txt"
    var fileNameSzeptember = "D:/Developing/workplace/stockmanager/src/main/resources/2022_09_01-09_30.txt"
    var szamlak = readReceipts(fileNameAprilis)
    println("Április:")
    var szervizDij = 0
    szamlak.filter { sz -> sz!!.kod.equals("szerviz") }.forEach { szamla -> szervizDij+=szamla!!.ar }
    println("Szervizek: ${szamlak.filter { sz -> sz!!.kod.equals("szerviz") }.size}")
    println("Szerviz díj összesen: $szervizDij")
    println("----------------")
    var szamlak08 = readReceipts(fileNameAugusztus)
    println("Augusztus:")
    szervizDij = 0
    szamlak08.filter { sz -> sz!!.kod.equals("szerviz") }.forEach { szamla -> szervizDij+=szamla!!.ar }
    println("Szervizek: ${szamlak08.filter { sz -> sz!!.kod.equals("szerviz") }.size}")
    println("Szerviz díj összesen: $szervizDij")
    println("----------------")
    var szamlak09 = readReceipts(fileNameSzeptember)
    println("Szeptember:")
    szervizDij = 0
    szamlak09.filter { sz -> sz!!.kod.equals("szerviz") }.forEach { szamla -> szervizDij+=szamla!!.ar }
    println("Szervizek: ${szamlak09.filter { sz -> sz!!.kod.equals("szerviz") }.size}")
    println("Szerviz díj összesen: $szervizDij")
}


fun readReceipts(fileName: String): List<SoldItem?> {
//    println(fileName)
    var lines = File(fileName).readLines()
    lines = lines.subList(1,lines.size)
    return lines.map { mapSzamla(it) }.filter { Objects.nonNull(it) }
}

fun mapSzamla(line: String): SoldItem? {
    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    println(line)
    var parts = line.split("\t")
    if (parts.size == 1) {
        return null
    }
    var sorszam = parts[0].toUpperCase()
    var datum = parts[1]
    var termek = parts[2]
    var kod = parts[3].filter { c -> c.isLetterOrDigit() || c.equals('-') }
    var tulajdonsag = Attribute.hungarian(parts[4])
    var mennyiseg = parts[5].filter { c -> c.isDigit() }.toInt()
    var ar = parts[7].filter { c -> c.isDigit() }.toInt()
    var fizetesiMod = parts[11]

    return SoldItem(sorszam, datum, termek, kod, tulajdonsag, mennyiseg, ar, fizetesiMod)
}



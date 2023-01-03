package com.bench.stockmanagement

import com.bench.stockmanagement.kotlin.model.BankiTranzakcio
import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime
import org.simpleframework.xml.Root
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import java.io.File

fun main(args: Array<String>) {
    //var fileName ="D:/Developing/workplace/stockmanager/src/main/resources/05_banki_kivonat.xls"
//    var fileName ="D:/Developing/workplace/stockmanager/src/main/resources/08_banki_kivonat.xml"
    var fileName ="D:/Developing/workplace/stockmanager/src/main/resources/07_banki_kivonat.csv"
    var tranzakciok = readFileAsLinesUsingUseLines(fileName)
    var kiadasok = tranzakciok.filter { it.tipus.equals("T") }
    kiadasok.forEach { println(it)}
}

fun xmlParsing(fileName:String): BkToCstmrAcctRpt{
//    var file = File("D:/Developing/workplace/stockmanager/src/main/resources/05_banki_kivonat.xml")
    var persister = Persister(AnnotationStrategy())
    return persister.read(BkToCstmrAcctRpt::class.java, fileName)
}
fun readFileLineByLineUsingForEachLine(fileName: String)
        = File(fileName).forEachLine { println(it) }
fun readFileAsLinesUsingUseLines(fileName: String): List<BankiTranzakcio> {
    var lines = File(fileName).readLines()
    return lines.map{mapLine(it)}
}

fun mapLine(line: String): BankiTranzakcio {
    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    var parts = line.split(";")
    var szamlaszam = parts[0]
    var tipus = parts[1]
    var osszeg = parts[2].toInt()
    var penznem = parts[3]
    var konyvelesDatum = parts[4]
    var tranzakcioDatum = parts[5]
    var egyenleg = parts[6].toInt()
//    var masikSzamlaszam = parts[7]
    var kedvezmenyezett = parts[8]
    var kozlemeny = parts[9]
    var megjegyzes = parts[10]

    return BankiTranzakcio(szamlaszam, tipus, osszeg, penznem, konyvelesDatum, tranzakcioDatum, egyenleg, kedvezmenyezett, kozlemeny, megjegyzes)
}

//"1173500526069364";J;41932;HUF;20220701;20220701;1953525;116000060000000066887724;"GPE-DIRECT MERCHANT";"7600105096/2440016896";"HITELKARTYA ELFOGADAS";;NAPKÖZBENI ÁTUTALÁS;;






@Root(strict = false)
data class BkToCstmrAcctRpt(@JsonProperty("GrpHdr")   private var grpHdr: GrpHdr? = null,
                            @JsonProperty("Rpt")   private var rpt: Rpt) {
}

data class GrpHdr(@JsonProperty("MsgId")    private var messageId: Long,
                  @JsonProperty("CreDtTm")    private var createDateTime: DateTime,
                  @JsonProperty("MsgPgntn")   private var pagination: MsgPgntn){}

data class MsgPgntn(@JsonProperty("PgNb")   private var pageNumber: Int,
                    @JsonProperty("LastPgInd")   private var lastPage: Boolean) {
}

data class Rpt(@JsonProperty("Id")   private var id: Long,
               @JsonProperty("CreDtTm")   private var createDateTime: DateTime,
               @JsonProperty("FrToDt")  private var intervar: FrToDt,
               @JsonProperty("RptgSrc") private var source: RptgSrc,
               @JsonProperty("Acct") 	private var account: Acct,
               @JsonProperty("Bal") private var bal: List<Bal>,
               @JsonProperty("TxsSummry") private var taxSummary: TxsSummry,
               @JsonProperty("Ntry")  private var entry: List<Ntry>) {
}

data class FrToDt(@JsonProperty ("FrDtTm") 	private var from: DateTime,
                  @JsonProperty ("ToDtTm") 	private var to: DateTime){}

data class RptgSrc(@JsonProperty("Prtry") 	private var prtry: String){}

data class Acct(@JsonProperty("Id") private var id: Id,
                @JsonProperty("Ownr") private var owner: Ownr,
                @JsonProperty("Svcr") private var svcr: Svcr){}

data class Id(@JsonProperty("Othr") private var othr: Othr){}

data class Othr(@JsonProperty("Id") private var id: Long){}

data class Ownr(@JsonProperty("Nm") private var name: String){}

data class Svcr(@JsonProperty("FinInstnId") private var finInstnId: FinInstnId){}

data class FinInstnId(@JsonProperty("Nm") private var name: String?,
                      @JsonProperty("PstlAdr") private var address: PstlAdr?,
                      @JsonProperty("BIC") private var bic: String?){}

data class PstlAdr(@JsonProperty("Ctry") private var country: String){}

data class Bal(@JsonProperty("Tp")  private var createDateTime: Tp,
               @JsonProperty("Amt")  private var amt: String,//???
@JsonProperty("CdtDbtInd") private var cdtDbtInd: String,
@JsonProperty("Dt") private var dt: Dt){}

data class Tp(@JsonProperty("CdOrPrtry") private var cdOrPrtry: CdOrPrtry){}

data class CdOrPrtry(@JsonProperty("Cd") private var cd: String){}

data class Dt(@JsonProperty("Dt") private var dt: String){}

data class TxsSummry(@JsonProperty("TtlCdtNtries") private var cdtEntries: TtlCdtNtries,
                     @JsonProperty("TtlDbtNtries") private var dbtEntries: TtlDbtNtries){}

data class TtlCdtNtries(@JsonProperty("NbOfNtries") private var numberOfEntries: Int,
                        @JsonProperty("Sum") private var sum: Int){}

data class TtlDbtNtries(@JsonProperty("NbOfNtries") private var numberOfEntries: Int,
                        @JsonProperty("Sum") private var sum: Int){}

data class Ntry(@JsonProperty("NtryRef") private var entryRef: Int,
                @JsonProperty("Amt") private var amount: Int,
                @JsonProperty("CdtDbtInd") private var cdtIndicator: String,
                @JsonProperty("Sts") private var sts: String,
                @JsonProperty("BookgDt") private var bookingDate: BookgDt,
                @JsonProperty("varDt") private var varDate: varDt,
                @JsonProperty("AcctSvcrRef") private var acctSvcrRef: Long,
                @JsonProperty("BkTxCd") private var bkTxCd: BkTxCd,
                @JsonProperty("NtryDtls") private var entryDetails: NtryDtls,
                @JsonProperty("AddtlNtryInf") private var additionalEntryInfo: String){
}

data class BookgDt(@JsonProperty("Dt") private var dt: String){}

data class varDt(@JsonProperty("Dt") private var dt: String){}

data class BkTxCd(@JsonProperty("Domn") private var domn: Domn,
                  @JsonProperty("Prtry") private var prtry: Prtry){}

data class Domn(@JsonProperty("cd") private var cd: Cd){}

data class Cd(@JsonProperty("Fmly") private var family: Fmly){}

data class Fmly(@JsonProperty("cd") private var cd: Int,
                @JsonProperty("SubFmlyCd") private var subFamilyCd: String){}

data class Prtry(@JsonProperty("Cd") private var cd: String?,
                 @JsonProperty("Tp") private var tp: String?,
                 @JsonProperty("Ref") private var ref: Int?){}

data class NtryDtls(@JsonProperty("TxDtls") private var txDetails: TxDtls){}

data class TxDtls(@JsonProperty("Refs") private var refs: Refs,
                  @JsonProperty("RltdPties") private var rltdPties: RltdPties,
                  @JsonProperty("RltdAgts") private var rltdAgts: RltdAgts,
                  @JsonProperty("Purp") private var purp: Purp,
                  @JsonProperty("RmtInf") private var rmtInfo: RmtInf,
                  @JsonProperty("RltdDts") private var rltdDetails: RltdDts,
                  @JsonProperty("AddtlTxInf") private var additionalTxInfo: String){}

data class Refs(@JsonProperty("EndToEndId") private var endToEndId: String,
                @JsonProperty("Prtry") private var prtry: RltdAgts){}

data class RltdPties(@JsonProperty("Dbtr") private var dbtr: Dbtr,
                     @JsonProperty("DbtrAcct") private var dbtrAcct: DbtrAcct,
                     @JsonProperty("UltmtDbtr") private var ultmtDbtr: UltmtDbtr,
                     @JsonProperty("Cdtr") private var cdtr: Cdtr,
                     @JsonProperty("CdtrAcct") private var cdtrAcct: CdtrAcct,
                     @JsonProperty("UltmtCdtr") private var ultmtCdtr: UltmtCdtr){}

data class Dbtr(@JsonProperty("Nm") private var nm: String,
                @JsonProperty("Id") private var id: Int?){}

data class DbtrAcct(@JsonProperty("Id") private var id: Id){}

data class UltmtDbtr(@JsonProperty("Nm") private var nm: String?,
                     @JsonProperty("Id") private var id: Int?){}

data class Cdtr(@JsonProperty("Nm") private var nm: String,
                @JsonProperty("Id") private var id: Int?){}

data class CdtrAcct(@JsonProperty("Id") private var id: Id){}

data class UltmtCdtr(@JsonProperty("Nm") private var nm: String?,
                     @JsonProperty("Id") private var id: Int?){}

data class RltdAgts(@JsonProperty("DbtrAgt") private var dbtrAgt: DbtrAgt){}

data class DbtrAgt(@JsonProperty("FinInstnId") private var finInstnId: FinInstnId){}

data class Purp(@JsonProperty("Cd") private var cd: String){}

data class RmtInf(@JsonProperty("Ustrd") private var ustrd: String){}

data class RltdDts(@JsonProperty("TxDtTm") private var txDateTime: DateTime){}
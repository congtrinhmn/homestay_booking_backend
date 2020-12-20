package com.ctr.homestaybooking

import com.google.gson.Gson
import com.mservice.allinone.processor.allinone.CaptureMoMo
import com.mservice.shared.constants.Parameter
import com.mservice.shared.sharedmodels.Environment
import com.mservice.shared.utils.Encoder
import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.model.Date
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.model.property.Version
import net.fortuna.ical4j.util.UidGenerator
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.util.*


/**
 * Demo
 */
object AllInOne {
    /***
     * Select environment
     * You can load config from file
     * MoMo only provide once endpoint for each envs: dev and prod
     * @param args
     * @throws
     */
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val requestId = "1605770899"
        var orderId = Calendar.getInstance().timeInMillis.toString()
        val amount: Long = 10000
        val orderInfo = "Thanh toan room"
        val returnURL = "https://momo.vn/"
        val notifyURL = "https://momo.vn/"
        val extraData = "merchantName=HomestayBooking"
        val bankCode = "SML"
        val customerNumber = "0963181714"
        val environment = Environment.selectEnv(Environment.EnvTarget.DEV, Environment.ProcessType.PAY_GATE)

        calendar()
//          Please uncomment the code to actually use the necessary All-In-One gateway payment processes
//          Remember to change the IDs
        val captureMoMoResponse = CaptureMoMo.process(environment, orderId, orderId, java.lang.Long.toString(amount), orderInfo, returnURL, notifyURL, extraData)
//        println("--= " + captureMoMoResponse)
//        val queryStatusTransactionResponse = QueryStatusTransaction.process(environment, orderId, orderId)
//        println("--= " + queryStatusTransactionResponse)

//          Refund -- Manual Testing
//            RefundMoMoResponse response = RefundMoMo.process(environment, "1562135830002", orderId, "10000", "2304963912");
//            RefundStatus.process(environment, "1562135830002", "1561972787557");
//
        val publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkpa+qMXS6O11x7jBGo9W3yxeHEsAdyDE" +
                "40UoXhoQf9K6attSIclTZMEGfq6gmJm2BogVJtPkjvri5/j9mBntA8qKMzzanSQaBEbr8FyByHnf226dsL" +
                "t1RbJSMLjCd3UC1n0Yq8KKvfHhvmvVbGcWfpgfo7iQTVmL0r1eQxzgnSq31EL1yYNMuaZjpHmQuT2" +
                "4Hmxl9W9enRtJyVTUhwKhtjOSOsR03sMnsckpFT9pn1/V9BE2Kf3rFGqc6JukXkqK6ZW9mtmGLSq3" +
                "K+JRRq2w8PVmcbcvTr/adW4EL2yc1qk9Ec4HtiDhtSYd6/ov8xLVkKAQjLVt7Ex3/agRPfPrNwIDAQAB"
        val privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCSlr6oxdLo7XXHuMEaj1bfLF4cSwB3IMTjRSheGhB/0rpq21IhyVNkwQZ+rqCYmbYGiBUm0+SO+uLn+P2YGe0DyoozPNqdJBoERuvwXIHIed/bbp2wu3VFslIwuMJ3dQLWfRirwoq98eG+a9VsZxZ+mB+juJBNWYvSvV5DHOCdKrfUQvXJg0y5pmOkeZC5PbgebGX1b16dG0nJVNSHAqG2M5I6xHTewyexySkVP2mfX9X0ETYp/esUapzom6ReSorplb2a2YYtKrcr4lFGrbDw9WZxty9Ov9p1bgQvbJzWqT0Rzge2IOG1Jh3r+i/zEtWQoBCMtW3sTHf9qBE98+s3AgMBAAECggEAQxBiU9aFgnk5HFGDTwJrDRlASRNrOBUu3odCS6MDD2e6T67daYWw+HRy4zxDTu1r4JsbijMA6wUPEG/SnWanD8f26DAcGC5vFKvZv5Ki8bQIXVzDGhr5MRS/E3lDxuEqljSPN+1+Ch6CV9r/vmN/YBV6zC1hH3IrTRPD71Jj1KMITCDQlKcDbZqgFTY0wq2ONrzQ5lF0u1sSrdnHLny2kayIAocWqSVbfcSE/9iKN4jkc2/zBQOAFgBQVPuZOdLL+rf1PTKus75aJm/TzaCcoxF496kTw/mRJ77rOxB8mNDEhGULTopG0Bk12upA+QXzxsWJKm8pgv/iXV+0Hi27oQKBgQDCMAydxOCybtOnTkRQ66typlRJQDVgBCD4yhNchOd6jWk34GRY64MuNbyyrD8A5P/ioI4OvRs00S28Sb/G/w3ldciR0j7lm9FgbjkTDCrVVbp4P8gczgL+z5mPdCua1KQD+2C5RA2tMRJlAfczIVekoxCriuCQSO9RltsGT7LmEQKBgQDBP/bzTD+PKWmxeBOTLeNGH8IM63DeccWtowxRgeF1xohFK1ipi5RKxoKOVLxku0U3tKOe6thE2IhpaqYFcCRs2TFZidChyytEjD4LVlECfe9OvCqfVL8IvDUzw8B3850HYrGUh8y4Mmry3JJYLOKoAPBqEg9NLe9c8yI9rI3UxwKBgGVQjnSOMLHH8vPaePhDTUtfDqC9OFvlK5LCU8G0sdUWDKyTjad7ERE+BjqudZyw3fTO0e9MqPIwpQ0U6VMY5ZYvkrrKF/jSCDaoq2yNr5doyAZPOMgWkCeEBtl6wflhMkXFlNx0bjJLZQ6ALQpnPgPu9BacObft5bcK3zF2yZ8RAoGBAIgkYfuBKf3XdQh7yX6Ug1qxoOmtLHTpvhPXnCQH1ig811+za+D13mDXfL5838QvUlIuRl78n6PQ0DlD0vZdzKuKT4P+3SY+lZrTGhqukp+ozOCxG23oLDUhMnHnZD6dN3EujGBRU14o1sOFtOu9o2gsUTLIylLbG5hmCSdd2wWdAoGBAIvddYHkS1b8B8TCv1+CVObe5WCUvqpZgbHo3oztD0KxlgWvl+f6y8DUToK3KU9sp512Ivk43mn1Xv2QftBx8E4vyhWeltdiKOJOhMsk6djjoyb8AOuyPVumXTQBuue1yRrTKLAl1SaZnzdrKzpXsI8OBpnI0bjFxA2SNnU/iD0R"
        val partnerCode = "MOMOIQA420180417"
        val phoneNumber = "0963181714"
        val username = "nhat.nguyen"
        orderId = System.currentTimeMillis().toString()
        //        PayATMResponse payATMResponse = PayATM.process(environment, requestId, orderId, bankCode, "35000", "Pay With MoMo", returnURL, notifyURL, "");

//        orderId = String.valueOf(System.currentTimeMillis());
//        RefundATM.process(environment, orderId, "1561972550332", "10000", "2304962904", bankCode);
//        RefundStatus.process(environment, "1562135830002", "1561972787557");
//
    }

    private fun calendar() {
        val calFile = "mycalendar.ics"

        //Creating a new calendar

        //Creating a new calendar
        var calendar = net.fortuna.ical4j.model.Calendar()
        calendar.properties.add(ProdId("-//Homestay Booking//iCal4j 1.0//EN"))
        calendar.properties.add(Version.VERSION_2_0)
        calendar.properties.add(CalScale.GREGORIAN)

//        val registry = TimeZoneRegistryFactory.getInstance().createRegistry()
//        val timezone: TimeZone = registry.getTimeZone("Australia/Melbourne")

//        val cal = Calendar.getInstance()
//        cal[Calendar.YEAR] = 2005
//        cal[Calendar.MONTH] = Calendar.NOVEMBER
//        cal[Calendar.DAY_OF_MONTH] = 1
//        cal[Calendar.HOUR_OF_DAY] = 15
//        cal.clear(Calendar.MINUTE)
//        cal.clear(Calendar.SECOND)
//
//        val dt = DateTime(cal.time)
//        val melbourneCup = VEvent(dt, "Melbourne Cup")

        //Creating an event

        //Creating an event
        val cal = Calendar.getInstance()
        cal[Calendar.MONTH] = Calendar.DECEMBER
        cal[Calendar.DAY_OF_MONTH] = 25

        val christmas = VEvent(Date(cal.time), Date(cal.time), "Christmas Day")
        // initialise as an all-day event..
        // initialise as an all-day event..
//        christmas.properties.getProperty<Property>(Property.DTSTART).parameters.add(Value.DATE)

        val uidGenerator = UidGenerator { Uid("id") }
        christmas.properties.add(uidGenerator.generateUid())
//        melbourneCup.properties.add(uidGenerator.generateUid())

        calendar.components.add(christmas)

        //Saving an iCalendar file

        //Saving an iCalendar file
        val fout = FileOutputStream(calFile)

        val outputter = CalendarOutputter()
        outputter.isValidating = false
        outputter.output(calendar, fout)

        //Now Parsing an iCalendar file

        /*//Now Parsing an iCalendar file
        val fin = FileInputStream(calFile)

        val builder = CalendarBuilder()

        var calendare = builder.build(fin)

        val i: Iterator<*> = calendare.components.iterator()
        while (i.hasNext()) {
            val component: Component = i.next() as Component
            println("Component [" + component.name.toString() + "]")
            val j: Iterator<*> = component.properties.iterator()
            while (j.hasNext()) {
                val property = j.next() as Property
                println("Property [" + property.name + ", " + property.value + "]")
            }
        }*/


    }

    //generate RSA signature from given information
    @Throws(Exception::class)
    fun generateRSA(phoneNumber: String, billId: String, transId: String, username: String, partnerCode: String, amount: Long, publicKey: String?): String {
        // current version of Parameter key name is 2.0
        val rawData: MutableMap<String, Any> = HashMap()
        rawData[Parameter.CUSTOMER_NUMBER] = phoneNumber
        rawData[Parameter.PARTNER_REF_ID] = billId
        rawData[Parameter.PARTNER_TRANS_ID] = transId
        rawData[Parameter.USERNAME] = username
        rawData[Parameter.PARTNER_CODE] = partnerCode
        rawData[Parameter.AMOUNT] = amount
        val gson = Gson()
        val jsonStr = gson.toJson(rawData)
        val testByte = jsonStr.toByteArray(StandardCharsets.UTF_8)
        return Encoder.encryptRSA(testByte, publicKey)
    }
}

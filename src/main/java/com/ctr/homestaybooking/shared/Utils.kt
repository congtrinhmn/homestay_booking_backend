package com.ctr.homestaybooking.shared

import com.google.gson.Gson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by at-trinhnguyen2 on 2020/11/02
 */
fun Date.datesUntil(endDate: Date): MutableSet<Date> {
    val dates = mutableSetOf<Date>()
    val calendar: Calendar = parse(this.format())
    val endCalendar: Calendar = parse(endDate.format())
    while (calendar.before(endCalendar)) {
        val result = parseDate(calendar.format())
        dates.add(result)
        calendar.add(Calendar.DATE, 1)
    }
    return dates
}

fun Date.add(days: Int): Date {
    val calendar: Calendar = parse(format())
    calendar.add(Calendar.DATE, days)
    return parseDate(calendar.format())
}

fun Date.isBefore(date: Date): Boolean {
    return TimeUnit.MILLISECONDS.toHours(date.time - this.time) >= 24
}

fun Date.isBeforeOneDate(date: Date): Boolean {
    return TimeUnit.MILLISECONDS.toHours(date.time - this.time) == 24.toLong()
}

fun Iterable<Date>.isContain(date: Date): Boolean {
    forEach {
        if (it.format(FORMAT_DATE) == date.format(FORMAT_DATE)) return true
    }
    return false
}

fun Iterable<Date>.isContainAll(dates: Iterable<Date>): Boolean {
    dates.forEach {
        if (!isContain(it)) return false
    }
    return true
}

fun List<Date>.consecutive(): MutableList<MutableList<Date>> {
    sortedBy { it.time }.apply {
        val result = mutableListOf<MutableList<Date>>()
        var temp = mutableListOf<Date>()
        temp.add(firstOrNull() ?: return mutableListOf(mutableListOf()))
        for (i in 0 until this.size - 1) {
            if (this[i].isBeforeOneDate(this[i + 1])) {
                temp.add(this[i + 1])
            } else {
                result.add(temp)
                temp = mutableListOf<Date>()
                temp.add(this[i + 1])
            }
        }
        result.add(temp)
        return result.apply { log.info { this } }
    }
}

fun parseDate(targetString: String, format: String = FORMAT_DATE_TIME): Date {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    try {
        return formatter.parse(targetString)
    } catch (e: ParseException) {
        throw IllegalArgumentException("non-parsable date format. target: $targetString, format: $format")
    }
}

fun parse(targetString: String, format: String = FORMAT_DATE_TIME): Calendar {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    try {
        val calendar = Calendar.getInstance()
        calendar.time = formatter.parse(targetString)
        return calendar
    } catch (e: ParseException) {
        throw IllegalArgumentException("non-parsable date format. target: $targetString, format: $format")
    }
}

fun Date.format(format: String = FORMAT_DATE_TIME): String {
    val resultFormat = SimpleDateFormat(format, Locale.getDefault())
    return resultFormat.format(time)
}

fun Calendar.format(format: String = FORMAT_DATE_TIME): String {
    val resultFormat = SimpleDateFormat(format, Locale.getDefault())
    return resultFormat.format(time)
}
//
//fun convert(targetString: String, targetFormat: String, resultFormat: String): String {
//    return parse(targetString, targetFormat).format(resultFormat)
//}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun Any.toJsonString(): String = Gson().toJson(this)

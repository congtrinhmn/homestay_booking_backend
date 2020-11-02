package com.ctr.homestaybooking.shared

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

fun Date.isBefore(date: Date): Boolean {
    return TimeUnit.MILLISECONDS.toHours(date.time - this.time) >= 24
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

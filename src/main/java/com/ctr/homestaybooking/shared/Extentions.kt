package com.ctr.homestaybooking.shared

import mu.KLogger
import mu.KotlinLogging
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/07/07
 */
fun <T : Any> Optional<T?>.toNullable(): T? = this.orElse(null)

val log = KotlinLogging.logger { }

fun <T : Any> T.logger(): KLogger {
    return KotlinLogging.logger { this.javaClass.name }
}



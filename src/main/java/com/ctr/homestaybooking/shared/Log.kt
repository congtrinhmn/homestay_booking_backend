package com.ctr.homestaybooking.shared

import mu.KLogger
import mu.KotlinLogging

/**
 * Created by at-trinhnguyen2 on 2020/07/07
 */
val log = KotlinLogging.logger { }

fun <T : Any> T.logger(): KLogger {
    return KotlinLogging.logger { this.javaClass.name }
}



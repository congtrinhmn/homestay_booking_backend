package com.ctr.homestaybooking.shared

import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/10/15
 */
internal const val FORMAT_DATE = "yyyy-MM-dd"
internal const val FORMAT_TIME = "'T'HH:mm:ss.SSSz"
internal const val FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSSz"
internal const val PATTERN_EMAIL = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\$"
internal const val PATTERN_PASSWORD = "(?=^.{8,}\$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*\$"
internal const val PATTERN_PHONE_NUMBER = "(0)+([0-9]{9})\\b"
internal const val ROLE_USER = "ROLE_USER"
internal const val ROLE_HOST = "ROLE_HOST"
internal const val ROLE_ADMIN = "ROLE_ADMIN"
internal const val BASE_URL_RECOMMEND = "https://homestay-booking-recommender.herokuapp.com/api"

fun <T : Any> Optional<T?>.toNullable(): T? = this.orElse(null)

package com.ctr.homestaybooking.shared

/**
 * Created by at-trinhnguyen2 on 2020/10/15
 */
internal const val FORMAT_DATE = "yyyy-MM-dd"
internal const val PATTERN_EMAIL = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\$"
internal const val PATTERN_PASSWORD = "(?=^.{8,}\$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*\$"
internal const val PATTERN_PHONE_NUMBER = "(0)+([0-9]{9})\\b"

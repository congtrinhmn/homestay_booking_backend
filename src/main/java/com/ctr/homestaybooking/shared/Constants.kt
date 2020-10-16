package com.ctr.homestaybooking.shared

object Constants {
    internal const val ACCESS_TOKEN_VALIDITY_SECONDS = 30 * 24 * 60 * 60.toLong()
    internal const val TOKEN_PREFIX = "Bearer "
    internal const val HEADER_STRING = "Authorization"
    internal const val AUTHORITIES_KEY = "scopes"
    internal const val KEY_USER_ID = "key_user_id"
    internal const val KEY_USER_UUID = "key_user_uuid"
}


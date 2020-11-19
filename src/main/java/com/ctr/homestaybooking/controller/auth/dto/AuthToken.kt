package com.ctr.homestaybooking.controller.auth.dto

import com.ctr.homestaybooking.entity.UserDetail

data class AuthToken(
        val userDetail: UserDetail,
        val token: String
)

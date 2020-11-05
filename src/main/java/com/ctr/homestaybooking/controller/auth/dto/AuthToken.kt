package com.ctr.homestaybooking.controller.auth.dto

import com.ctr.homestaybooking.entity.UserDetailDto

data class AuthToken(
        val userDetailDto: UserDetailDto,
        val token: String
)

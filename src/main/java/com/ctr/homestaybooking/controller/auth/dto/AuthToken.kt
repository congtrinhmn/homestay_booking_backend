package com.ctr.homestaybooking.controller.auth.dto

import com.ctr.homestaybooking.controller.user.dto.UserDetailResponse

data class AuthToken(
        val userDetailResponse: UserDetailResponse,
        val token: String
)

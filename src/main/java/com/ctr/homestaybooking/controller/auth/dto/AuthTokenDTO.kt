package com.ctr.homestaybooking.controller.auth.dto

import com.ctr.homestaybooking.controller.user.dto.UserDetailResponse

data class AuthTokenDTO(
        val userDTO: UserDetailResponse,
        val token: String
)

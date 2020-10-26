package com.ctr.homestaybooking.controller.auth.dto

import com.ctr.homestaybooking.controller.user.dto.UserDto

data class AuthTokenDTO(
        val userDTO: UserDto,
        val token: String
)

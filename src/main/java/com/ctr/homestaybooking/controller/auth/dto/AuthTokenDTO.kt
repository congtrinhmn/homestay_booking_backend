package com.ctr.homestaybooking.controller.auth.dto

import com.ctr.homestaybooking.controller.user.dto.UserDTO

data class AuthTokenDTO(
        val userDTO: UserDTO,
        val token: String
)

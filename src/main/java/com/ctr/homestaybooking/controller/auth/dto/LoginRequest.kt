package com.ctr.homestaybooking.controller.auth.dto

import com.ctr.homestaybooking.shared.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


data class LoginRequest(
        @field: NotBlank(message = NOT_BLANK_EMAIL)
        @field: Pattern(regexp = PATTERN_EMAIL, message = ERROR_EMAIL)
        val email: String,

        @field: NotBlank(message = NOT_BLANK_PASSWORD)
        @field: Pattern(regexp = PATTERN_PASSWORD, message = ERROR_PASSWORD)
        val password: String,

        val deviceToken: String
)

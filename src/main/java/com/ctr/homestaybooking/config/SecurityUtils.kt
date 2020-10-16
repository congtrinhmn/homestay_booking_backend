package com.ctr.homestaybooking.config

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt

object SecurityUtils {
    val currentUserEmail: String?
        get() {
            if (SecurityContextHolder.getContext().authentication != null && SecurityContextHolder.getContext()
                            .authentication
                            .principal is Jwt) {
                val jwt = SecurityContextHolder
                        .getContext()
                        .authentication
                        .principal as Jwt
                return if (jwt.claims["sub"] != null) jwt.claims["sub"].toString() else null
            }
            return SecurityContextHolder.getContext().authentication.name
        }
}

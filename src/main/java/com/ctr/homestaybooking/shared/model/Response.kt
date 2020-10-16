package com.ctr.homestaybooking.shared.model

import org.springframework.http.ResponseEntity

object Response {
    fun buildResponseError(apiError: ApiError): ResponseEntity<Any> {
        return ResponseEntity(apiError, apiError.status)
    }
}

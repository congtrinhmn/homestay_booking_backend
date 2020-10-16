package com.ctr.homestaybooking.controller.auth.exception

import com.ctr.homestaybooking.shared.model.ApiError
import com.ctr.homestaybooking.shared.model.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class AuthExceptionController {
    @ExceptionHandler(value = [EmailLoginFailedException::class])
    fun exception(exception: EmailLoginFailedException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.FORBIDDEN, exception))
    }

    @ExceptionHandler(value = [PasswordLoginFailedException::class])
    fun exception(exception: PasswordLoginFailedException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.FORBIDDEN, exception))
    }

    @ExceptionHandler(value = [InActiveStatusUserException::class])
    fun exception(exception: InActiveStatusUserException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.FORBIDDEN, exception))
    }
}

class EmailLoginFailedException : RuntimeException("Email login failed")

class InActiveStatusUserException(username: String) : RuntimeException("Status user $username inActive status")

class PasswordLoginFailedException : RuntimeException("Password login failed")


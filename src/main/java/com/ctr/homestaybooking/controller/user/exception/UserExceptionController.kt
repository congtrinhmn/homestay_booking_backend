package com.ctr.homestaybooking.controller.user.exception

import com.ctr.homestaybooking.shared.model.ApiError
import com.ctr.homestaybooking.shared.model.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class UserExceptionController {
    @ExceptionHandler(value = [UserNotFoundException::class])
    fun exception(exception: UserNotFoundException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.NOT_FOUND, exception))
    }

    @ExceptionHandler(value = [UserIsExistsException::class])
    fun exception(exception: UserIsExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.CONFLICT, exception))
    }

    @ExceptionHandler(value = [UserIsNotExistsException::class])
    fun exception(exception: UserIsNotExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.BAD_GATEWAY, exception))
    }
}

class UserNotFoundException(id: Int) : RuntimeException("User id not found : $id")

class UserIsNotExistsException(id: Int) : RuntimeException("User is not exist with id: $id")

class UserIsExistsException(id: Int) : RuntimeException("User is exist with id: $id")

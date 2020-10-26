package com.ctr.homestaybooking.config

import com.ctr.homestaybooking.shared.model.ApiError
import com.ctr.homestaybooking.shared.model.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Created by at-trinhnguyen2 on 2020/10/26
 */

@ControllerAdvice
class ExceptionController {
    @ExceptionHandler(value = [NotFoundException::class])
    fun exception(exception: NotFoundException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.NOT_FOUND, exception))
    }

    @ExceptionHandler(value = [IsExistsException::class])
    fun exception(exception: IsExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.CONFLICT, exception))
    }

    @ExceptionHandler(value = [IsNotExistsException::class])
    fun exception(exception: IsNotExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.BAD_GATEWAY, exception))
    }
}

class NotFoundException(`class`: String, id: Int) : RuntimeException("$`class` not found : $id")

class IsExistsException(`class`: String, id: Int) : RuntimeException("$`class` is exist with id: $id")

class IsNotExistsException(`class`: String, id: Int) : RuntimeException("$`class` is not exist with id: $id")


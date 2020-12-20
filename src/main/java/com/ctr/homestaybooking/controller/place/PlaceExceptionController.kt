package com.ctr.homestaybooking.controller.place

import com.ctr.homestaybooking.shared.model.ApiError
import com.ctr.homestaybooking.shared.model.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Created by at-trinhnguyen2 on 2020/10/19
 */

@ControllerAdvice
class PlaceExceptionController {
    @ExceptionHandler(value = [PlaceNotFoundException::class])
    fun exception(exception: PlaceNotFoundException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.NOT_FOUND, exception))
    }

    @ExceptionHandler(value = [PlaceSearchNotFoundException::class])
    fun exception(exception: PlaceSearchNotFoundException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.NOT_FOUND, exception))
    }

    @ExceptionHandler(value = [PlaceIsExistsException::class])
    fun exception(exception: PlaceIsExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.CONFLICT, exception))
    }

    @ExceptionHandler(value = [PlaceIsNotExistsException::class])
    fun exception(exception: PlaceIsNotExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.BAD_GATEWAY, exception))
    }

}

class PlaceNotFoundException(id: Int) : RuntimeException("Place id not found : $id")

class PlaceSearchNotFoundException(text: String) : RuntimeException("Place not found : $text")

class PlaceIsExistsException(id: Int) : RuntimeException("Place is exist with id: $id")

class PlaceIsNotExistsException(id: Int) : RuntimeException("Place is not exist with id: $id")


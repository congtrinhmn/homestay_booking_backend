package com.ctr.homestaybooking.controller.placetype

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
class PlaceTypeExceptionController {
    @ExceptionHandler(value = [PlaceTypeNotFoundException::class])
    fun exception(exception: PlaceTypeNotFoundException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.NOT_FOUND, exception))
    }

    @ExceptionHandler(value = [PlaceTypeIsExistsException::class])
    fun exception(exception: PlaceTypeIsExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.CONFLICT, exception))
    }

    @ExceptionHandler(value = [PlaceTypeIsNotExistsException::class])
    fun exception(exception: PlaceTypeIsNotExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.BAD_GATEWAY, exception))
    }
}

class PlaceTypeNotFoundException(id: Int) : RuntimeException("Place type id not found : $id")

class PlaceTypeIsExistsException(id: Int) : RuntimeException("Place type is exist with id: $id")

class PlaceTypeIsNotExistsException(id: Int) : RuntimeException("Place type is not exist with id: $id")


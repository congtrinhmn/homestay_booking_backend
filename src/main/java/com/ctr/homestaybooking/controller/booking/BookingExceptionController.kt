package com.ctr.homestaybooking.controller.booking

import com.ctr.homestaybooking.controller.placetype.PlaceTypeIsExistsException
import com.ctr.homestaybooking.controller.placetype.PlaceTypeIsNotExistsException
import com.ctr.homestaybooking.controller.placetype.PlaceTypeNotFoundException
import com.ctr.homestaybooking.shared.model.ApiError
import com.ctr.homestaybooking.shared.model.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */

@ControllerAdvice
class BookingExceptionController {
    @ExceptionHandler(value = [BookingNotFoundException::class])
    fun exception(exception: PlaceTypeNotFoundException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.NOT_FOUND, exception))
    }

    @ExceptionHandler(value = [BookingIsExistsException::class])
    fun exception(exception: PlaceTypeIsExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.CONFLICT, exception))
    }

    @ExceptionHandler(value = [BookingIsNotExistsException::class])
    fun exception(exception: PlaceTypeIsNotExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.BAD_GATEWAY, exception))
    }
}

class BookingNotFoundException(id: Int) : RuntimeException("Booking id not found : $id")

class BookingIsExistsException(id: Int) : RuntimeException("Booking is exist with id: $id")

class BookingIsNotExistsException(id: Int) : RuntimeException("Booking is not exist with id: $id")
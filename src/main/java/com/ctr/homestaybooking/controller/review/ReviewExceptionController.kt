package com.ctr.homestaybooking.controller.review

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
class ReviewExceptionController {
    @ExceptionHandler(value = [ReviewNotFoundException::class])
    fun exception(exception: ReviewNotFoundException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.NOT_FOUND, exception))
    }

    @ExceptionHandler(value = [ReviewIsExistsException::class])
    fun exception(exception: ReviewIsExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.CONFLICT, exception))
    }

    @ExceptionHandler(value = [ReviewIsDuplicateException::class])
    fun exception(exception: ReviewIsDuplicateException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.CONFLICT, exception))
    }

    @ExceptionHandler(value = [ReviewIsNotExistsException::class])
    fun exception(exception: ReviewIsNotExistsException): ResponseEntity<Any> {
        return Response.buildResponseError(ApiError(HttpStatus.BAD_GATEWAY, exception))
    }
}

class ReviewNotFoundException(id: Int) : RuntimeException("Review id not found : $id")

class ReviewIsExistsException(id: Int) : RuntimeException("Review is exist with id: $id")

class ReviewIsDuplicateException(id: Int) : RuntimeException("Review is duplicate with booking id: $id")

class ReviewIsNotExistsException(id: Int) : RuntimeException("Review is not exist with id: $id")

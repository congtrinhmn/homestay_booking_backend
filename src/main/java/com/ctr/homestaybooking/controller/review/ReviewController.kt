package com.ctr.homestaybooking.controller.review

import com.ctr.homestaybooking.entity.ReviewRequest
import com.ctr.homestaybooking.entity.ReviewResponse
import com.ctr.homestaybooking.service.BookingService
import com.ctr.homestaybooking.service.PlaceService
import com.ctr.homestaybooking.service.ReviewService
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * Created by at-trinhnguyen2 on 2020/11/03
 */
@RestController
@RequestMapping("/api/reviews")
class ReviewController(private val reviewService: ReviewService,
                       private val userService: UserService,
                       private val placeService: PlaceService,
                       private val bookingService: BookingService
) {
    @get:GetMapping
    val allReviewDetail: ApiData<List<ReviewResponse>>
        get() = ApiData(reviewService.getAllReview().map { it.toReviewResponse() })

    @GetMapping("/{id}")
    fun getReviewById(@PathVariable("id") id: Int): ApiData<ReviewResponse> {
        return ApiData(reviewService.getReviewById(id).toReviewResponse())
    }

    @PostMapping
    fun addReview(@RequestBody @Validated reviewRequest: ReviewRequest): ApiData<ReviewResponse> {
        return ApiData(reviewService.addReview(reviewRequest.toReviewEntity(userService, placeService, bookingService)).toReviewResponse())
    }

    @PutMapping
    fun editReview(@RequestBody @Validated reviewRequest: ReviewRequest): ApiData<ReviewResponse> {
        return ApiData(reviewService.editReview(reviewRequest.toReviewEntity(userService, placeService, bookingService)).toReviewResponse())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable("id") id: Int) {
        reviewService.deleteReviewByID(id)
    }
}

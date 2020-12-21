package com.ctr.homestaybooking.controller.review

import com.ctr.homestaybooking.entity.Review
import com.ctr.homestaybooking.entity.ReviewBody
import com.ctr.homestaybooking.service.BookingService
import com.ctr.homestaybooking.service.ReviewService
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
                       private val bookingService: BookingService
) {
    @get:GetMapping
    val allReviewDetail: ApiData<List<Review>>
        get() = ApiData(reviewService.getAllReview().map { it.toReview() })

    @GetMapping("/{id}")
    fun getReviewById(@PathVariable("id") id: Int): ApiData<Review> {
        return ApiData(reviewService.getReviewById(id).toReview())
    }

    @GetMapping("/host/{id}")
    fun getReviewByHostId(@PathVariable("id") id: Int): ApiData<List<Review>> {
        return ApiData(reviewService.getReviewsHostId(id).map { it.toReview() })
    }

    @PostMapping
    fun addReview(@RequestBody @Validated reviewBody: ReviewBody): ApiData<Review> {
        return ApiData(reviewService.addReview(reviewBody.toReviewEntity(bookingService)).toReview())
    }

    @PutMapping
    fun editReview(@RequestBody @Validated reviewBody: ReviewBody): ApiData<Review> {
        return ApiData(reviewService.editReview(reviewBody.toReviewEntity(bookingService)).toReview())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable("id") id: Int) {
        reviewService.deleteReviewByID(id)
    }
}

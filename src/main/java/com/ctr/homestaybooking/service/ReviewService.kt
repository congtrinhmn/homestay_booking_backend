package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.review.ReviewIsDuplicateException
import com.ctr.homestaybooking.controller.review.ReviewNotFoundException
import com.ctr.homestaybooking.entity.ReviewEntity
import com.ctr.homestaybooking.repository.ReviewRepository
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.stereotype.Service

/**
 * Created by at-trinhnguyen2 on 2020/11/03
 */
@Service
class ReviewService(private val reviewRepository: ReviewRepository) {

    fun getAllReview() = reviewRepository.findAll().filterIsInstance<ReviewEntity>()

    fun getReviewById(id: Int) = reviewRepository.findById(id).toNullable()
            ?: throw ReviewNotFoundException(id)

    fun addReview(reviewEntity: ReviewEntity): ReviewEntity {
        reviewEntity.bookingEntity?.let {
            if (reviewRepository.existsById(it.id)) {
                throw  ReviewIsDuplicateException(it.id)
            }
        }

        return reviewRepository.save(reviewEntity)
    }

    fun editReview(reviewEntity: ReviewEntity): ReviewEntity {
        if (reviewRepository.findById(reviewEntity.id).toNullable() == null)
            throw ReviewNotFoundException(reviewEntity.id)

        return reviewRepository.save(reviewEntity)
    }

    fun deleteReviewByID(id: Int): ReviewEntity {
        val reviewEntity: ReviewEntity = reviewRepository.findById(id).toNullable()
                ?: throw ReviewNotFoundException(id)
        reviewRepository.delete(reviewEntity)
        return reviewEntity
    }
}

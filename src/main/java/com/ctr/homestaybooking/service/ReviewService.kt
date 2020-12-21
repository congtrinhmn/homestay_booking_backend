package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.review.ReviewIsDuplicateException
import com.ctr.homestaybooking.controller.review.ReviewNotFoundException
import com.ctr.homestaybooking.controller.user.UserNotFoundException
import com.ctr.homestaybooking.entity.ReviewEntity
import com.ctr.homestaybooking.repository.BookingRepository
import com.ctr.homestaybooking.repository.ReviewRepository
import com.ctr.homestaybooking.repository.UserRepository
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.stereotype.Service

/**
 * Created by at-trinhnguyen2 on 2020/11/03
 */
@Service
class ReviewService(private val reviewRepository: ReviewRepository,
                    private val bookingRepository: BookingRepository,
                    private val userRepository: UserRepository
) {

    fun getAllReview() = reviewRepository.findAll().filterIsInstance<ReviewEntity>()

    fun getReviewById(id: Int) = reviewRepository.findById(id).toNullable()
            ?: throw ReviewNotFoundException(id)

    fun getReviewsHostId(id: Int): List<ReviewEntity> {
        val userEntity = userRepository.findById(id).toNullable() ?: throw UserNotFoundException(id)
        return reviewRepository.findByUserEntity(userEntity)
    }

    fun addReview(reviewEntity: ReviewEntity): ReviewEntity {
        reviewEntity.bookingEntity?.let {
            if (it.reviewEntity != null) {
                throw  ReviewIsDuplicateException(it.id)
            }
        }

        return reviewRepository.save(reviewEntity).apply {
            reviewEntity.bookingEntity?.id?.let { id ->
                bookingRepository.findById(id).toNullable()?.let {
                    it.isReview = true
                    bookingRepository.save(it)
                }
            }
        }
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

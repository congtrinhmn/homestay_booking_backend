package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.place.dto.PlaceDetailResponse
import com.ctr.homestaybooking.controller.user.dto.UserDetailResponse
import com.ctr.homestaybooking.service.BookingService
import com.ctr.homestaybooking.service.PlaceService
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.FORMAT_DATE_TIME
import com.ctr.homestaybooking.shared.enums.ReviewStatus
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*

/**
 * Created by at-trinhnguyen2 on 20020/11/02
 */
@Entity
@Table(name = "reviews")
class ReviewEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        var comment: String,

        var rating: Int,

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var createDate: Date,

        @Enumerated(EnumType.STRING)
        var status: ReviewStatus = ReviewStatus.LISTED,

        @ManyToOne
        @JoinColumn(name = "review_user_id")
        var userEntity: UserEntity,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "place_id")
        var placeEntity: PlaceEntity? = null,

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "booking_id", unique = true)
        var bookingEntity: BookingEntity? = null
) {
    fun toReviewResponse() = ReviewResponse(id, comment, rating, createDate, userEntity.toUserDetailResponse())

    fun toReviewDetailResponse() = ReviewDetailResponse(id, comment, rating, createDate, userEntity.toUserDetailResponse(), placeEntity?.toPlaceDetailResponse(), bookingEntity?.toBookingDetailResponse())
}

data class ReviewResponse(var id: Int,
                          var comment: String,
                          var rating: Int,
                          var createDate: Date,
                          var user: UserDetailResponse
)

data class ReviewDetailResponse(var id: Int,
                                var comment: String,
                                var rating: Int,
                                var createDate: Date,
                                var user: UserDetailResponse,
                                var place: PlaceDetailResponse?,
                                var booking: BookingDetailResponse?)

data class ReviewRequest(
        var id: Int,
        var comment: String,
        var rating: Int,
        var createDate: Date,
        var userId: Int,
        var placeId: Int,
        var bookingId: Int
) {
    fun toReviewEntity(userService: UserService, placeService: PlaceService, bookingService: BookingService) = ReviewEntity(
            id = id,
            comment = comment,
            rating = rating,
            createDate = createDate,
            userEntity = userService.getUserById(userId),
            placeEntity = placeService.getPlaceByID(placeId),
            bookingEntity = bookingService.getBookingById(bookingId)
    )
}

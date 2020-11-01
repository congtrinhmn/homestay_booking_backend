package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.place.dto.PlaceDetailResponse
import com.ctr.homestaybooking.controller.user.dto.UserDetailResponse
import com.ctr.homestaybooking.service.PlaceService
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.FORMAT_DATE
import com.ctr.homestaybooking.shared.FORMAT_DATE_TIME
import com.ctr.homestaybooking.shared.enums.BookingStatus
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*

/**
 * Created by at-trinhnguyen2 on 2020/10/27
 */
@Entity
@Table(name = "bookings")
class BookingEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var createDate: Date,

        @Temporal(TemporalType.DATE)
        @DateTimeFormat(pattern = FORMAT_DATE)
        var startDate: Date,

        @Temporal(TemporalType.DATE)
        @DateTimeFormat(pattern = FORMAT_DATE)
        var endDate: Date,

        var pricePerDay: Double,

        var priceForStay: Double,

        var taxPaid: Double,

        var totalPaid: Double,

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var cancerDate: Date?,

        var isRefund: Boolean = false,

        var refundPaid: Double?,

        @Enumerated(EnumType.STRING)
        var bookingStatus: BookingStatus,

        @ManyToOne
        @JoinColumn(name = "place_id")
        var placeEntity: PlaceEntity,

        @ManyToOne
        @JoinColumn(name = "user_id")
        var userEntity: UserEntity
) {
    fun toBookingDetailResponse() = BookingDetailResponse(
            id = id,
            createDate = createDate,
            startDate = startDate,
            endDate = endDate,
            pricePerDay = pricePerDay,
            priceForStay = priceForStay,
            taxPaid = taxPaid,
            totalPaid = totalPaid,
            cancerDate = null,
            isRefund = false,
            refundPaid = null,
            bookingStatus = bookingStatus,
            place = placeEntity.toPlaceDetailResponse(),
            user = userEntity.toUserDetailResponse()
    )
}

data class BookingRequest(
        var createDate: Date,
        var startDate: Date,
        var endDate: Date,
        var pricePerDay: Double,
        var priceForStay: Double,
        var taxPaid: Double,
        var totalPaid: Double,
        var bookingStatus: BookingStatus,
        var placeId: Int,
        var userId: Int
) {
    fun toBookingEntity(placeService: PlaceService, userService: UserService) = BookingEntity(
            id = 0,
            createDate = createDate,
            startDate = startDate,
            endDate = endDate,
            pricePerDay = pricePerDay,
            priceForStay = priceForStay,
            taxPaid = taxPaid,
            totalPaid = totalPaid,
            cancerDate = null,
            isRefund = false,
            refundPaid = null,
            bookingStatus = bookingStatus,
            placeEntity = placeService.getPlaceByID(placeId),
            userEntity = userService.getUserById(userId)
    )
}

data class BookingDetailResponse(
        var id: Int,
        var createDate: Date,
        var startDate: Date,
        var endDate: Date,
        var pricePerDay: Double,
        var priceForStay: Double,
        var taxPaid: Double,
        var totalPaid: Double,
        var cancerDate: Date?,
        var isRefund: Boolean = false,
        var refundPaid: Double?,
        var bookingStatus: BookingStatus,
        var place: PlaceDetailResponse,
        var user: UserDetailResponse
)

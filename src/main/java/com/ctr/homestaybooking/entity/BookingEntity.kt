package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.place.dto.PlaceDetail
import com.ctr.homestaybooking.service.PlaceService
import com.ctr.homestaybooking.service.PromoService
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

        var isRefund: Boolean? = false,

        var refundPaid: Double?,

        @Enumerated(EnumType.STRING)
        var bookingStatus: BookingStatus,

        @ManyToOne
        @JoinColumn(name = "place_id")
        var placeEntity: PlaceEntity,

        @ManyToOne
        @JoinColumn(name = "user_id")
        var userEntity: UserEntity,

        @ManyToOne
        @JoinColumn(name = "promo_id")
        var promoEntity: PromoEntity?,

        @OneToOne(mappedBy = "bookingEntity")
        var reviewEntity: ReviewEntity? = null,

        var orderId: String? = null,

        var transId: String? = null
) {
    fun toBookingDetail() = BookingDetail(
            id = id,
            createDate = createDate,
            startDate = startDate,
            endDate = endDate,
            pricePerDay = pricePerDay,
            priceForStay = priceForStay,
            taxPaid = taxPaid,
            totalPaid = totalPaid,
            cancerDate = cancerDate,
            isRefund = isRefund,
            refundPaid = refundPaid,
            status = bookingStatus,
            place = placeEntity.toPlaceDetail(),
            user = userEntity.toUserDetail(),
            promo = promoEntity?.toPromo(),
            review = reviewEntity?.toReview(),
            orderId = orderId,
            transId = transId
    )
    override fun toString() = toBookingDetail().toString()
}

data class BookingDetail(
        var id: Int,
        var createDate: Date,
        var startDate: Date,
        var endDate: Date,
        var pricePerDay: Double,
        var priceForStay: Double,
        var taxPaid: Double,
        var totalPaid: Double,
        var cancerDate: Date?,
        var isRefund: Boolean? = false,
        var refundPaid: Double?,
        var status: BookingStatus,
        var place: PlaceDetail,
        var user: UserDetail,
        var promo: Promo?,
        var review: Review?,
        var orderId: String?,
        var transId: String?
)

data class BookingBody(
        var id: Int,
        var startDate: Date,
        var endDate: Date,
        var pricePerDay: Double,
        var priceForStay: Double,
        var taxPaid: Double,
        var totalPaid: Double,
        var status: BookingStatus,
        var placeId: Int,
        var userId: Int,
        var promoId: Int?
) {
    fun toBookingEntity(placeService: PlaceService, userService: UserService, promoService: PromoService) = BookingEntity(
            id = id,
            createDate = Date(),
            startDate = startDate,
            endDate = endDate,
            pricePerDay = pricePerDay,
            priceForStay = priceForStay,
            taxPaid = taxPaid,
            totalPaid = totalPaid,
            cancerDate = null,
            isRefund = false,
            refundPaid = null,
            bookingStatus = status,
            placeEntity = placeService.getPlaceByID(placeId),
            userEntity = userService.getUserById(userId),
            promoEntity = promoId?.let { promoService.getPromoById(it) }
    )
}

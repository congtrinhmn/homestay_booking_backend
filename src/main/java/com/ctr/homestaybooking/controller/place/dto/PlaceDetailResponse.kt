package com.ctr.homestaybooking.controller.place.dto

import com.ctr.homestaybooking.controller.user.dto.UserDetailResponse
import com.ctr.homestaybooking.entity.*
import com.ctr.homestaybooking.shared.enums.BookingType
import com.ctr.homestaybooking.shared.enums.CancelType
import com.ctr.homestaybooking.shared.enums.PlaceStatus
import com.ctr.homestaybooking.shared.enums.SubmitStatus
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/10/26
 */

data class PlaceDetailResponse(

        var id: Int = 0,

        var name: String?,

        var description: String?,

        var bookingType: BookingType?,

        var longitude: String?,

        var latitude: String?,

        var street: String?,

        var address: String?,

        var guestCount: Int?,

        var roomCount: Int?,

        var bedCount: Int?,

        var bathroomCount: Int?,

        var size: Int?,

        var pricePerDay: Double?,

        var cancelType: CancelType?,

        var earliestCheckInTime: Date?,

        var latestCheckInTime: Date?,

        var checkOutTime: Date?,

        var submitStatus: SubmitStatus = SubmitStatus.DRAFT,

        var status: PlaceStatus = PlaceStatus.UNLISTED,

        var images: Set<String>?,

        var amenities: Set<AmenityEntity>?,

        var bookingSlots: Set<BookingSlotEntity>,

        var hostDetail: UserDetailResponse?,

        var wardDetailResponse: WardDetailResponse?,

        var placeTypeId: Int?,

        var promos: Set<PromoResponse>?,

        var reviews: Set<ReviewResponse>?,

        var rateCount: Int = 0,

        var rateAverage: String = "0.0"
)

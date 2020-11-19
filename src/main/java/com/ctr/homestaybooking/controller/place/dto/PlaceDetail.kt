package com.ctr.homestaybooking.controller.place.dto

import com.ctr.homestaybooking.entity.*
import com.ctr.homestaybooking.shared.enums.BookingType
import com.ctr.homestaybooking.shared.enums.CancelType
import com.ctr.homestaybooking.shared.enums.PlaceStatus
import com.ctr.homestaybooking.shared.enums.SubmitStatus
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/10/26
 */

data class PlaceDetail(

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

        var submitStatus: SubmitStatus?,

        var status: PlaceStatus?,

        var images: List<String>?,

        var amenities: List<Int>?,

        var bookingSlots: List<BookingSlot>?,

        var hostDetail: UserDetail?,

        var wardDetail: WardDetail?,

        var placeType: String?,

        var promos: List<Promo>?,

        var reviews: List<Review>?,

        var rateCount: Int = 0,

        var rateAverage: Double = 0.0
)

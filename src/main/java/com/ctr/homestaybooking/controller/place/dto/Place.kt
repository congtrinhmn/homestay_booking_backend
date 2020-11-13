package com.ctr.homestaybooking.controller.place.dto

import com.ctr.homestaybooking.shared.enums.BookingType

/**
 * Created by at-trinhnguyen2 on 2020/10/26
 */

data class Place(

        var id: Int = 0,

        var name: String?,

        var description: String?,

        var bookingType: BookingType?,

        var street: String?,

        var address: String?,

        var guestCount: Int?,

        var roomCount: Int?,

        var bedCount: Int?,

        var bathroomCount: Int?,

        var size: Int?,

        var pricePerDay: Double?,

        var images: List<String>?,

        var placeType: String?,

        var rateCount: Int = 0,

        var rateAverage: Double = 0.0
)

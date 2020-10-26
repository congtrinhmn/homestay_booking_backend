package com.ctr.homestaybooking.controller.place.dto

import com.ctr.homestaybooking.entity.AmenityEntity
import com.ctr.homestaybooking.entity.BookingSlotEntity
import com.ctr.homestaybooking.shared.enums.BookingType

/**
 * Created by at-trinhnguyen2 on 2020/10/26
 */

data class PlaceDetailResponse(

        var id: Int = 0,

        var name: String?,

        var description: String?,

        var bookingType: BookingType?,

        var street: String?,

        var address: String?,

        var guest_count: Int?,

        var room_count: Int?,

        var bed_count: Int?,

        var bathroom_count: Int?,

        var size: Int?,

        var price_per_day: Double?,

        var images: Set<String>?,

        var amenityEntity: Set<AmenityEntity>?,

        var bookingSlots: Set<BookingSlotEntity>?
)

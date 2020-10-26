package com.ctr.homestaybooking.controller.place.dto

import com.ctr.homestaybooking.entity.BookingSlotEntity
import com.ctr.homestaybooking.entity.ImageEntity
import com.ctr.homestaybooking.entity.PlaceEntity
import com.ctr.homestaybooking.service.AmenityService
import com.ctr.homestaybooking.service.LocationService
import com.ctr.homestaybooking.service.PlaceTypeService
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.enums.BookingType
import com.ctr.homestaybooking.shared.enums.PlaceStatus
import com.ctr.homestaybooking.shared.enums.RefundType
import java.util.*
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotNull


/**
 * Created by at-trinhnguyen2 on 2020/10/19
 */
data class PlaceRequest(

        var id: Int = 0,

        var name: String,

        var description: String?,

        var bookingType: BookingType?,

        var longitude: String?,

        var latitude: String?,

        var street: String?,

        var address: String?,

        var guest_count: Int?,

        var room_count: Int?,

        var bed_count: Int?,

        var bathroom_count: Int?,

        var size: Int?,

        var price_per_day: Double?,

        @Enumerated(EnumType.STRING)
        var refund_type: RefundType?,

        var earliest_check_in_time: Date?,

        var latest_check_in_time: Date?,

        var check_out_time: Date?,

        @Enumerated(EnumType.STRING)
        var status: PlaceStatus = PlaceStatus.CREATING,

        var images: List<String>?,

        var amenities: List<Int>?,

        var bookingSlots: Set<Date>?,

        @NotNull
        var userId: Int,

        var wardId: Int?,

        var placeTypeId: Int?

) {

    fun toPlaceEntity(userService: UserService,
                      locationService: LocationService,
                      placeTypeService: PlaceTypeService,
                      amenityService: AmenityService
    ) = PlaceEntity(
            id,
            name,
            description,
            bookingType,
            longitude,
            latitude,
            street,
            address,
            guest_count,
            room_count,
            bed_count,
            bathroom_count,
            size,
            price_per_day,
            refund_type,
            earliest_check_in_time,
            latest_check_in_time,
            check_out_time,
            status,
            images?.map { ImageEntity(url = it) }?.toSet(),
            amenities?.map { amenityService.getAmenityById(it) }?.toSet(),
            bookingSlots?.map { BookingSlotEntity(0, it) }?.toSet(),
            userService.getUserById(userId),
            wardId?.let { locationService.getWardById(it) },
            placeTypeId?.let { placeTypeService.getPlaceTypeById(it) }
    )
}


package com.ctr.homestaybooking.controller.place.dto

import com.ctr.homestaybooking.entity.BookingSlotDto
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

        var guestCount: Int?,

        var roomCount: Int?,

        var bedCount: Int?,

        var bathroomCount: Int?,

        var size: Int?,

        var pricePerDay: Double?,

        @Enumerated(EnumType.STRING)
        var refundType: RefundType?,

        var earliestCheckInTime: Date?,

        var latestCheckInTime: Date?,

        var checkOutTime: Date?,

        @Enumerated(EnumType.STRING)
        var status: PlaceStatus = PlaceStatus.CREATING,

        var images: List<String>?,

        var amenities: List<Int>?,

        var bookingSlots: Set<BookingSlotDto>?,

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
            guestCount,
            roomCount,
            bedCount,
            bathroomCount,
            size,
            pricePerDay,
            refundType,
            earliestCheckInTime,
            latestCheckInTime,
            checkOutTime,
            status,
            images?.map { ImageEntity(url = it) }?.toSet(),
            amenities?.map { amenityService.getAmenityById(it) }?.toSet(),
            bookingSlots?.map { BookingSlotEntity(0, it.date, it.status) }?.toSet(),
            userService.getUserById(userId),
            wardId?.let { locationService.getWardById(it) },
            placeTypeId?.let { placeTypeService.getPlaceTypeById(it) }
    )
}


package com.ctr.homestaybooking.controller.place.dto

import com.ctr.homestaybooking.entity.BookingSlot
import com.ctr.homestaybooking.entity.BookingSlotEntity
import com.ctr.homestaybooking.entity.ImageEntity
import com.ctr.homestaybooking.entity.PlaceEntity
import com.ctr.homestaybooking.service.AmenityService
import com.ctr.homestaybooking.service.LocationService
import com.ctr.homestaybooking.service.PlaceTypeService
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.FORMAT_DATE_TIME
import com.ctr.homestaybooking.shared.enums.BookingType
import com.ctr.homestaybooking.shared.enums.CancelType
import com.ctr.homestaybooking.shared.enums.PlaceStatus
import com.ctr.homestaybooking.shared.enums.SubmitStatus
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.validation.constraints.NotNull


/**
 * Created by at-trinhnguyen2 on 2020/10/19
 */
data class PlaceBody(

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

        @Enumerated(EnumType.STRING)
        var cancelType: CancelType?,

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var earliestCheckInTime: Date?,

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var latestCheckInTime: Date?,

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var checkOutTime: Date?,

        @Enumerated(EnumType.STRING)
        var submitStatus: SubmitStatus = SubmitStatus.DRAFT,

        var status: PlaceStatus = PlaceStatus.UNLISTED,

        var images: List<String>?,

        var amenities: List<Int>?,

        var bookingSlots: Set<BookingSlot>?,

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
            id = id,
            name = name,
            description = description,
            bookingType = bookingType,
            longitude = longitude,
            latitude = latitude,
            street = street,
            address = address,
            guestCount = guestCount,
            roomCount = roomCount,
            bedCount = bedCount,
            bathroomCount = bathroomCount,
            size = size,
            pricePerDay = pricePerDay,
            cancelType = cancelType,
            earliestCheckInTime = earliestCheckInTime,
            latestCheckInTime = latestCheckInTime,
            checkOutTime = checkOutTime,
            submitStatus = submitStatus,
            status = status,
            imageEntities = images?.map { ImageEntity(url = it) }?.toSet(),
            amenityEntities = amenities?.map { amenityService.getAmenityById(it) }?.toSet(),
            bookingSlotEntities = bookingSlots?.map { BookingSlotEntity(0, it.date, it.status) }?.toMutableSet()
                    ?: mutableSetOf(),
            userEntity = userService.getUserById(userId),
            wardEntity = wardId?.let { locationService.getWardById(it) },
            placeTypeEntity = placeTypeId?.let { placeTypeService.getPlaceTypeById(it) },
            promoEntities = null,
            reviewEntities = setOf()
    )
}


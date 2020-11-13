package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.place.dto.Place
import com.ctr.homestaybooking.controller.place.dto.PlaceDetail
import com.ctr.homestaybooking.shared.FORMAT_DATE_TIME
import com.ctr.homestaybooking.shared.enums.BookingType
import com.ctr.homestaybooking.shared.enums.CancelType
import com.ctr.homestaybooking.shared.enums.PlaceStatus
import com.ctr.homestaybooking.shared.enums.SubmitStatus
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


/**
 * Created by at-trinhnguyen2 on 2020/10/16
 */
@Entity
@Table(name = "places")
class PlaceEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @NotNull
        var name: String?,

        @Column(columnDefinition = "text")
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
        var submitStatus: SubmitStatus?,

        @Enumerated(EnumType.STRING)
        var status: PlaceStatus?,

        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
        var imageEntities: Set<ImageEntity>?,

        @ManyToMany
        @JoinTable(
                name = "place_amenity",
                joinColumns = [JoinColumn(name = "place_id")],
                inverseJoinColumns = [JoinColumn(name = "amenity_id")])
        var amenityEntities: Set<AmenityEntity>?,

        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
        @JoinColumn(name = "place_id")
        var bookingSlotEntities: MutableSet<BookingSlotEntity>?,

        @ManyToOne
        @JoinColumn(name = "host_id")
        var userEntity: UserEntity?,

        @ManyToOne
        @JoinColumn(name = "ward_id")
        var wardEntity: WardEntity?,

        @ManyToOne
        @JoinColumn(name = "place_type_id")
        var placeTypeEntity: PlaceTypeEntity?,

        @ManyToMany(mappedBy = "placeEntities")
        var promoEntities: Set<PromoEntity>?,

        @OneToMany(mappedBy = "placeEntity", orphanRemoval = true)
        var reviewEntities: Set<ReviewEntity>?
) {
    @PreRemove
    private fun removePlaceFromPromo() {
        promoEntities?.forEach {
            it.placeEntities.remove(this)
        }
    }

    fun toPlace() = Place(
            id = id,
            name = name,
            description = description,
            bookingType = bookingType,
            street = street,
            address = address,
            guestCount = guestCount,
            roomCount = roomCount,
            bedCount = bedCount,
            bathroomCount = bathroomCount,
            size = size,
            pricePerDay = pricePerDay,
            images = imageEntities?.sortedBy { it.id }?.map { it.url },
            placeType = placeTypeEntity?.name,
            rateCount = reviewEntities?.size ?: 0,
            rateAverage = if (reviewEntities.isNullOrEmpty()) 0.0 else reviewEntities?.let { reviews -> reviews.sumBy { it.rating }.toDouble().div(reviews.size) }
                    ?: 0.0
    )

    fun toPlaceDetail() = PlaceDetail(
            id = id,
            name = name,
            description = description,
            bookingType = bookingType,
            latitude = latitude,
            longitude = longitude,
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
            images = imageEntities?.sortedBy { it.id }?.map { it.url },
            amenities = amenityEntities?.sortedBy { it.id }?.map { it.id },
            bookingSlots = bookingSlotEntities?.sortedBy { it.date }?.map { it.toBookingSlot() },
            hostDetail = userEntity?.toUserDetail(),
            wardDetail = wardEntity?.toWardDetail(),
            placeType = placeTypeEntity?.name,
            promos = promoEntities?.filter { it.startDate.before(Date()) && it.endDate.after(Date()) }
                    ?.sortedBy { it.discountPercent }
                    ?.map { it.toPromo() },
            reviews = reviewEntities?.map { it.toReview() },
            rateCount = reviewEntities?.size ?: 0,
            rateAverage = if (reviewEntities.isNullOrEmpty()) 0.0 else reviewEntities?.let { reviews -> reviews.sumBy { it.rating }.toDouble().div(reviews.size) }
                    ?: 0.0
    )

    override fun toString() = toPlaceDetail().toString()
}

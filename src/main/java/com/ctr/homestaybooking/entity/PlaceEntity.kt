package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.place.dto.PlaceDetailResponse
import com.ctr.homestaybooking.controller.place.dto.PlaceResponse
import com.ctr.homestaybooking.shared.FORMAT_TIME
import com.ctr.homestaybooking.shared.enums.BookingType
import com.ctr.homestaybooking.shared.enums.PlaceStatus
import com.ctr.homestaybooking.shared.enums.RefundType
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by at-trinhnguyen2 on 2020/10/16
 */
@Entity
@Table(name = "places")
data class PlaceEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @NotNull
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

        @Temporal(TemporalType.TIME)
        @DateTimeFormat(pattern = FORMAT_TIME)
        var earliest_check_in_time: Date?,

        @Temporal(TemporalType.TIME)
        @DateTimeFormat(pattern = FORMAT_TIME)
        var latest_check_in_time: Date?,

        @Temporal(TemporalType.TIME)
        @DateTimeFormat(pattern = FORMAT_TIME)
        var check_out_time: Date?,

        @Enumerated(EnumType.STRING)
        var status: PlaceStatus = PlaceStatus.CREATING,

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
        var bookingSlotEntities: Set<BookingSlotEntity>?,

        @ManyToOne
        @JoinColumn(name = "host_id")
        var userEntity: UserEntity?,

        @ManyToOne
        @JoinColumn(name = "ward_id")
        var wardEntity: WardEntity?,

        @ManyToOne
        @JoinColumn(name = "place_type_id")
        var placeTypeEntity: PlaceTypeEntity?
) {
        fun toPlaceResponse() = PlaceResponse(
                id,
                name,
                description,
                bookingType,
                street,
                address,
                guest_count,
                room_count,
                bed_count,
                bathroom_count,
                size,
                price_per_day,
                imageEntities?.map { it.url }?.toSet()
        )

        fun toPlaceDetailResponse() = PlaceDetailResponse(
                id,
                name,
                description,
                bookingType,
                street,
                address,
                guest_count,
                room_count,
                bed_count,
                bathroom_count,
                size,
                price_per_day,
                imageEntities?.map { it.url }?.toSet(),
                amenityEntities,
                bookingSlotEntities
        )
}

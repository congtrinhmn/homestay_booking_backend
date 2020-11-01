package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.place.dto.PlaceResponse
import com.ctr.homestaybooking.service.PlaceService
import com.ctr.homestaybooking.shared.FORMAT_DATE_TIME
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */

@Entity
@Table(name = "promos")
class PromoEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @NotNull
        var name: String,

        @NotNull
        var description: String = "",

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var createDate: Date,

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var startDate: Date,

        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var endDate: Date,

        var discount: Double?,

        var discountPercent: Double?,

        @ManyToMany(cascade = [CascadeType.MERGE])
        @JoinTable(name = "promo_place",
                joinColumns = [JoinColumn(name = "promo_id")],
                inverseJoinColumns = [JoinColumn(name = "place_id")])
        var placeEntities: MutableSet<PlaceEntity>
) {

    fun toPromoResponse() = PromoResponse(
            id = id,
            name = name,
            description = description,
            createDate = createDate,
            startDate = startDate,
            endDate = endDate,
            discount = discount,
            discountPercent = discountPercent
    )

    fun toPromoDetailResponse() = PromoDetailResponse(
            id = id,
            name = name,
            description = description,
            createDate = createDate,
            startDate = startDate,
            endDate = endDate,
            discount = discount,
            discountPercent = discountPercent,
            places = placeEntities.map { it.toPlaceResponse() }.toSet()
    )
}

data class PromoResponse(
        var id: Int,
        var name: String,
        var description: String = "",
        var createDate: Date,
        var startDate: Date,
        var endDate: Date,
        var discount: Double?,
        var discountPercent: Double?
)

data class PromoDetailResponse(
        var id: Int,
        var name: String,
        var description: String = "",
        var createDate: Date,
        var startDate: Date,
        var endDate: Date,
        var discount: Double?,
        var discountPercent: Double?,
        var places: Set<PlaceResponse>?
)

data class PromoRequest(
        var id: Int = 0,
        var name: String,
        var description: String = "",
        var createDate: Date,
        var startDate: Date,
        var endDate: Date,
        var discount: Double?,
        var discountPercent: Double?,
        var placeIDs: Set<Int>
) {
    fun toPromoEntity(placeService: PlaceService) = PromoEntity(
            id = id,
            name = name,
            description = description,
            createDate = createDate,
            startDate = startDate,
            endDate = endDate,
            discount = discount,
            discountPercent = discountPercent,
            placeEntities = placeService.getPlacesByIDs(placeIDs).toMutableSet()
    )
}

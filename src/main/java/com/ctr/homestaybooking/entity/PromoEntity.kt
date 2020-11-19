package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.place.dto.Place
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

        @NotNull
        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var createDate: Date,

        @NotNull
        @Temporal(TemporalType.TIMESTAMP)
        @DateTimeFormat(pattern = FORMAT_DATE_TIME)
        var startDate: Date,

        @NotNull
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

    fun toPromo() = Promo(
            id = id,
            name = name,
            description = description,
            createDate = createDate,
            startDate = startDate,
            endDate = endDate,
            discount = discount,
            discountPercent = discountPercent
    )

    fun toPromoDetail() = PromoDetail(
            id = id,
            name = name,
            description = description,
            createDate = createDate,
            startDate = startDate,
            endDate = endDate,
            discount = discount,
            discountPercent = discountPercent,
            places = placeEntities.map { it.toPlace() }.toSet()
    )
}

data class Promo(
        var id: Int,
        var name: String,
        var description: String = "",
        var createDate: Date,
        var startDate: Date,
        var endDate: Date,
        var discount: Double?,
        var discountPercent: Double?
)

data class PromoDetail(
        var id: Int,
        var name: String,
        var description: String = "",
        var createDate: Date,
        var startDate: Date,
        var endDate: Date,
        var discount: Double?,
        var discountPercent: Double?,
        var places: Set<Place>?
)

data class PromoBody(
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

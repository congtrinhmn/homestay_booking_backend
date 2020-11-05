package com.ctr.homestaybooking.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by at-trinhnguyen2 on 2020/10/16
 */
@Entity
@Table(name = "place_type")
data class PlaceTypeEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @NotNull
        var name: String,

        @NotNull
        var description: String
) {
    fun toPlaceTypeDto() = PlaceTypeDto(id, name, description)
}

data class PlaceTypeDto(var id: Int = 0, var name: String, var description: String = "")

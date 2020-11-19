package com.ctr.homestaybooking.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by at-trinhnguyen2 on 2020/10/16
 */
@Entity
@Table(name = "wards")
class WardEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @NotNull
        var type: String,

        @NotNull
        var name: String,

//        @OneToMany(mappedBy = "wardEntity")
//        var placeEntities: List<PlaceEntity>? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "district_id")
        var districtEntity: DistrictEntity? = null

) {
    fun toWard() = Ward(id, type, name)

    fun toWardDetail() = WardDetail(id, type, name, districtEntity?.toDistrictDetail())
}

data class Ward(var id: Int = 0, var type: String, var name: String)

data class WardDetail(var id: Int = 0, var type: String, var name: String, var districtDetail: DistrictDetail?)


package com.ctr.homestaybooking.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by at-trinhnguyen2 on 2020/10/16
 */
@Entity
@Table(name = "districts")
class DistrictEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @NotNull
        var type: String,

        @NotNull
        var name: String,

        @OneToMany(mappedBy = "districtEntity")
        var wardEntities: List<WardEntity>? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "province_id")
        var provinceEntity: ProvinceEntity? = null
) {
    fun toDistrictResponse() = DistrictResponse(id, type, name, wardEntities?.map { it.toWardResponse() })

    fun toDistrictDetailResponse() = DistrictDetailResponse(id, type, name, provinceEntity?.toProvinceResponse())
}

data class DistrictResponse(var id: Int = 0, var type: String, var name: String, val wards: List<WardResponse>?)

data class DistrictDetailResponse(var id: Int = 0, var type: String, var name: String, val provinceResponse: ProvinceResponse?)

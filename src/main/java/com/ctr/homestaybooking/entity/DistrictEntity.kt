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
    fun toDistrictDto() = DistrictDto(id, type, name, wardEntities?.map { it.toWardDto() })

    fun toDistrictDetailDto() = DistrictDetailDto(id, type, name, provinceEntity?.toProvinceResponse())
}

data class DistrictDto(var id: Int = 0, var type: String, var name: String, val wards: List<WardDto>?)

data class DistrictDetailDto(var id: Int = 0, var type: String, var name: String, val provinceDto: ProvinceDto?)

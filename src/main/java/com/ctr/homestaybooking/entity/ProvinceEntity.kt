package com.ctr.homestaybooking.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by at-trinhnguyen2 on 2020/10/16
 */
@Entity
@Table(name = "provinces")
class ProvinceEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @NotNull
        var type: String,

        @NotNull
        var name: String,

        @OneToMany(mappedBy = "provinceEntity")
        var districtEntities: List<DistrictEntity>? = null
) {
    fun toProvinceResponse() = ProvinceDto(id, type, name)

    fun toProvinceDetailResponse() = ProvinceDetailDto(id, type, name, districtEntities?.map { it.toDistrictDto() })
}

data class ProvinceDto(var id: Int = 0, var type: String, var name: String)

data class ProvinceDetailDto(var id: Int = 0, var type: String, var name: String, var districts: List<DistrictDto>?)

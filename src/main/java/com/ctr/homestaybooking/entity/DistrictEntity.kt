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
    fun toDistrict() = District(id, type, name, wardEntities?.map { it.toWard() })

    fun toDistrictDetail() = DistrictDetail(id, type, name, provinceEntity?.toProvince())
}

data class District(var id: Int = 0, var type: String, var name: String, val wards: List<Ward>?)

data class DistrictDetail(var id: Int = 0, var type: String, var name: String, val province: Province?)

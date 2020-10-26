package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.entity.DistrictEntity
import com.ctr.homestaybooking.entity.ProvinceEntity
import com.ctr.homestaybooking.entity.WardEntity
import com.ctr.homestaybooking.repository.DistrictRepository
import com.ctr.homestaybooking.repository.ProvinceRepository
import com.ctr.homestaybooking.repository.WardRepository
import org.springframework.stereotype.Service

/**
 * Created by at-trinhnguyen2 on 2020/10/21
 */
@Service
class LocationService(private val provinceRepository: ProvinceRepository,
                      private val districtRepository: DistrictRepository,
                      private val wardRepository: WardRepository
) {

    fun getProvinces(): List<ProvinceEntity> = provinceRepository.findAll().filterIsInstance<ProvinceEntity>()

    fun getProvinceById(id: Int): ProvinceEntity = provinceRepository.getOne(id)

    fun getDistrictsByProvinceId(id: Int): List<DistrictEntity> = provinceRepository.getOne(id).districtEntities

    fun getDistrictById(id: Int): DistrictEntity = districtRepository.getOne(id)

    fun getWardsByDistrictId(id: Int): List<WardEntity> = getDistrictById(id).wardEntities

    fun getWardById(id: Int): WardEntity = wardRepository.getOne(id)
}

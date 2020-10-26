package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.placetype.PlaceTypeNotFoundException
import com.ctr.homestaybooking.entity.PlaceTypeEntity
import com.ctr.homestaybooking.repository.PlaceTypeRepository
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.stereotype.Service

/**
 * Created by at-trinhnguyen2 on 2020/10/22
 */
@Service
class PlaceTypeService(private val placeTypeRepository: PlaceTypeRepository) {

    fun getAllPlaceType() = placeTypeRepository.findAll().filterIsInstance<PlaceTypeEntity>()

    fun getPlaceTypeById(id: Int) = placeTypeRepository.findById(id).toNullable()
            ?: throw PlaceTypeNotFoundException(id)
}

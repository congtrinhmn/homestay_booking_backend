package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.config.NotFoundException
import com.ctr.homestaybooking.entity.AmenityEntity
import com.ctr.homestaybooking.repository.AmenityRepository
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.stereotype.Service

/**
 * Created by at-trinhnguyen2 on 2020/10/26
 */
@Service
class AmenityService(private val amenityRepository: AmenityRepository) {

    fun getAllAmenity() = amenityRepository.findAll().filterIsInstance<AmenityEntity>()

    fun getAmenityById(id: Int) = amenityRepository.findById(id).toNullable()
            ?: throw NotFoundException("Amenity", id)
}

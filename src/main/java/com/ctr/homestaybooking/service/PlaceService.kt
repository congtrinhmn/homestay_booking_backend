package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.config.NotFoundException
import com.ctr.homestaybooking.controller.place.PlaceNotFoundException
import com.ctr.homestaybooking.entity.PlaceEntity
import com.ctr.homestaybooking.entity.WardEntity
import com.ctr.homestaybooking.repository.PlaceRepository
import com.ctr.homestaybooking.repository.PlaceTypeRepository
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.stereotype.Service

/**
 * Created by at-trinhnguyen2 on 2020/10/19
 */
@Service
class PlaceService(private val placeRepository: PlaceRepository,
                   private val placeTypeRepository: PlaceTypeRepository
) {

    fun getAllPlace(): List<PlaceEntity> = placeRepository.findAll().filterIsInstance<PlaceEntity>()

    fun getPlaceByID(id: Int): PlaceEntity =
            placeRepository.findById(id).toNullable() ?: throw  PlaceNotFoundException(id)

    fun getPlacesByWardEntity(wardEntity: WardEntity): Set<PlaceEntity> =
            placeRepository.findByWardEntity(wardEntity) ?: throw  NotFoundException("Ward", wardEntity.id)

    fun getPlacesByDistrictId(id: Int): Set<PlaceEntity> =
            placeRepository.findByDistrictId(id) ?: throw  NotFoundException("District", id)

    fun addPlace(placeEntity: PlaceEntity): PlaceEntity {
        return placeRepository.save(placeEntity)
    }

    fun editPlace(placeEntity: PlaceEntity): PlaceEntity {
        if (placeRepository.findById(placeEntity.id).toNullable() == null)
            throw PlaceNotFoundException(placeEntity.id)

        return placeRepository.save(placeEntity)
    }

    fun deletePlaceByID(id: Int): PlaceEntity {
        val placeEntity: PlaceEntity = placeRepository.findById(id).toNullable()
                ?: throw PlaceNotFoundException(id)
        placeRepository.delete(placeEntity)
        return placeEntity
    }
}

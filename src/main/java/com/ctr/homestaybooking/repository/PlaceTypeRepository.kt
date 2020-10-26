package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.PlaceTypeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PlaceTypeRepository : JpaRepository<PlaceTypeEntity?, Int?> {
    fun findByName(name: String): PlaceTypeEntity?
}

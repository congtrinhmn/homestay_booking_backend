package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.AmenityEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AmenityRepository : JpaRepository<AmenityEntity?, Int?> {
    fun findByName(name: String): AmenityEntity?
}

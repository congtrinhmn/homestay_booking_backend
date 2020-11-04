package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.DistrictEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DistrictRepository : JpaRepository<DistrictEntity, Int> {
    fun findByName(name: String): DistrictEntity?
}

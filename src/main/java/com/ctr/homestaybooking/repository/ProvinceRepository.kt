package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.ProvinceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProvinceRepository : JpaRepository<ProvinceEntity?, Int?> {
    fun findByName(name: String): ProvinceEntity?
}

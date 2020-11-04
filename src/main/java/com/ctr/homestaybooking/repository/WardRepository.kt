package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.WardEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WardRepository : JpaRepository<WardEntity, Int> {
    fun findByName(name: String): WardEntity?
}

package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<RoleEntity, Int> {
    fun findByName(name: String): RoleEntity?
}

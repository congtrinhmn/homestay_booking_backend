package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.PromoEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */
interface PromoRepository : JpaRepository<PromoEntity, Int> {
    fun findByName(name: String): PromoEntity?
}

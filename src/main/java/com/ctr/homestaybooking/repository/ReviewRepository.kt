package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.ReviewEntity
import com.ctr.homestaybooking.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by at-trinhnguyen2 on 2020/11/03
 */
interface ReviewRepository : JpaRepository<ReviewEntity, Int> {
    fun findByUserEntity(userEntity: UserEntity): List<ReviewEntity>

    fun countByUserEntity(userEntity: UserEntity): List<ReviewEntity>
}

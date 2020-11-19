package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.BookingEntity
import com.ctr.homestaybooking.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */
interface BookingRepository : JpaRepository<BookingEntity, Int> {
    fun findByUserEntity(userEntity: UserEntity): Set<BookingEntity>
}

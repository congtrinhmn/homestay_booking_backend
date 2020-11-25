package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.BookingEntity
import com.ctr.homestaybooking.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */
interface BookingRepository : JpaRepository<BookingEntity, Int> {
    fun findByUserEntity(userEntity: UserEntity): Set<BookingEntity>

    @Query("""select * from bookings b 
                    inner join places p on p.id = b.place_id 
                    inner join users u on u.id = p.host_id 
                    where u.id = :id""", nativeQuery = true)
    fun findByHostId(id: Int): Set<BookingEntity>
}

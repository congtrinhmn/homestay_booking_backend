package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.BookingSlotEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookingSlotRepository : JpaRepository<BookingSlotEntity, Int> {
    fun findByDate(date: Date): BookingSlotEntity?
}

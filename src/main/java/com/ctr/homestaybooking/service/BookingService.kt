package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.booking.BookingNotFoundException
import com.ctr.homestaybooking.entity.BookingEntity
import com.ctr.homestaybooking.repository.BookingRepository
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.stereotype.Service

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */
@Service
class BookingService(private val bookingRepository: BookingRepository) {

    fun getAllBooking() = bookingRepository.findAll().filterIsInstance<BookingEntity>()

    fun getBookingById(id: Int) = bookingRepository.findById(id).toNullable()
            ?: throw BookingNotFoundException(id)

    fun addBooking(bookingEntity: BookingEntity): BookingEntity {
        return bookingRepository.save(bookingEntity)
    }

    fun editBooking(bookingEntity: BookingEntity): BookingEntity {
        if (bookingRepository.findById(bookingEntity.id).toNullable() == null)
            throw BookingNotFoundException(bookingEntity.id)

        return bookingRepository.save(bookingEntity)
    }

    fun deleteBookingByID(id: Int): BookingEntity {
        val bookingEntity: BookingEntity = bookingRepository.findById(id).toNullable()
                ?: throw BookingNotFoundException(id)
        bookingRepository.delete(bookingEntity)
        return bookingEntity
    }
}

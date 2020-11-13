package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.booking.BookingNotFoundException
import com.ctr.homestaybooking.controller.booking.ConflictException
import com.ctr.homestaybooking.entity.BookingEntity
import com.ctr.homestaybooking.repository.BookingRepository
import com.ctr.homestaybooking.shared.*
import com.ctr.homestaybooking.shared.enums.BookingStatus
import com.ctr.homestaybooking.shared.enums.DateStatus
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */
@Service
class BookingService(private val bookingRepository: BookingRepository,
                     private val placeService: PlaceService,
                     private val userService: UserService
) {

    fun getAllBooking() = bookingRepository.findAll().filterIsInstance<BookingEntity>()

    fun getBookingByUserId(id: Int) = bookingRepository.findByUserEntity(userService.getUserById(id))

    fun getBookingById(id: Int) = bookingRepository.findById(id).toNullable()
            ?: throw BookingNotFoundException(id)

    fun addBooking(bookingEntity: BookingEntity): BookingEntity {
        val now = Date()
        bookingEntity.apply {
            log.info { bookingEntity }
            // check startDate must be before endDate
            if (!startDate.isBefore(endDate)) {
                throw ConflictException("Start date must be before end date")
            }

            // check booking dates is available
            val bookingDates = startDate.datesUntil(endDate)
            placeService.getPlaceByID(bookingEntity.placeEntity.id).let { place ->
                val availableDates = place.bookingSlotEntities?.filter { it.status == DateStatus.AVAILABLE }?.map { it.date }
                        ?: listOf()
                if (!availableDates.isContainAll(bookingDates)) {
                    throw ConflictException("Booking dates is unavailable")
                }

                // check promo is valid
                bookingEntity.promoEntity?.id?.let { id ->
                    (place.promoEntities?.find { it.id == id }
                            ?: throw ConflictException("Promo code is invalid")).apply {

                        if (now.before(startDate)) {
                            throw ConflictException("Promo code is not start")
                        }

                        if (now.after(endDate)) {
                            throw ConflictException("Promo code is expired")
                        }
                    }
                }
            }
            return bookingRepository.save(bookingEntity).apply {
                log.info { bookingEntity.placeEntity.id }
                placeService.updateBookingSlotById(placeEntity.id, bookingDates)
            }
        }
    }

    fun editBooking(bookingEntity: BookingEntity): BookingEntity {
        if (bookingRepository.findById(bookingEntity.id).toNullable() == null)
            throw BookingNotFoundException(bookingEntity.id)

        return bookingRepository.save(bookingEntity)
    }

    fun changeBookingStatus(id: Int, bookingStatus: BookingStatus): BookingEntity {
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        bookingEntity.bookingStatus = bookingStatus
        return bookingRepository.save(bookingEntity)
    }

    fun deleteBookingByID(id: Int) {
        if (bookingRepository.findById(id).toNullable() == null)
            throw BookingNotFoundException(id)
        return bookingRepository.deleteById(id)
    }
}

package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.booking.*
import com.ctr.homestaybooking.entity.BookingEntity
import com.ctr.homestaybooking.repository.BookingRepository
import com.ctr.homestaybooking.shared.*
import com.ctr.homestaybooking.shared.enums.BookingStatus
import com.ctr.homestaybooking.shared.enums.DateStatus
import com.mservice.allinone.models.CaptureMoMoResponse
import com.mservice.allinone.models.QueryStatusTransactionResponse
import com.mservice.allinone.models.RefundMoMoResponse
import com.mservice.allinone.processor.allinone.CaptureMoMo
import com.mservice.allinone.processor.allinone.QueryStatusTransaction
import com.mservice.allinone.processor.allinone.RefundMoMo
import com.mservice.shared.sharedmodels.Environment
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
    private val environment = Environment.selectEnv(Environment.EnvTarget.DEV, Environment.ProcessType.PAY_GATE)

    fun getAllBooking() = bookingRepository.findAll().filterIsInstance<BookingEntity>()

    fun getBookingByUserId(id: Int) = bookingRepository.findByUserEntity(userService.getUserById(id))

    fun getBookingByHostId(id: Int) = bookingRepository.findByHostId(id)

    fun getBookingById(id: Int) = bookingRepository.findById(id).toNullable()
            ?: throw BookingNotFoundException(id)

    fun addBooking(bookingEntity: BookingEntity): BookingEntity {
        val now = Date()
        bookingEntity.apply {
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

            if (bookingEntity.placeEntity.userEntity?.id == bookingEntity.userEntity.id) {
                throw ConflictException("Can't book your place!")
            }

            return bookingRepository.save(bookingEntity).apply {
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
        if (bookingStatus == BookingStatus.PAID) throw MethodNotAllowedException()
        bookingEntity.bookingStatus = bookingStatus
        return bookingRepository.save(bookingEntity)
    }

    fun deleteBookingByID(id: Int) {
        if (bookingRepository.findById(id).toNullable() == null)
            throw BookingNotFoundException(id)
        return bookingRepository.deleteById(id)
    }


    fun requestPayment(id: Int): CaptureMoMoResponse {
        val extraData = "merchantName=HomestayBooking"
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        val now = Calendar.getInstance().timeInMillis
        bookingEntity.apply {
            var captureMoMoResponse: CaptureMoMoResponse? = null
            repeat(5) {
                if (captureMoMoResponse == null || captureMoMoResponse!!.payUrl.isEmpty() && captureMoMoResponse!!.orderId.isEmpty()) {
                    captureMoMoResponse = CaptureMoMo.process(
                            environment,
                            now.toString(),
                            now.toString(),
                            totalPaid.toInt().toString(),
                            placeEntity.name,
                            "homestay://payment/$id",
                            "https://homestay-booking.herokuapp.com/api/bookings/$id/paid",
                            extraData).apply { log.info { "captureMoMoResponse: " + captureMoMoResponse } }
                } else {
                    bookingEntity.orderId = captureMoMoResponse!!.orderId.toLong()
                    bookingRepository.save(bookingEntity)
                    return@repeat
                }
            }
            return captureMoMoResponse ?: throw MethodNotSuccess()
        }
    }

    fun changeBookingStatusPaid(id: Int): BookingEntity {
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        bookingEntity.bookingStatus = BookingStatus.PAID
        return bookingRepository.save(bookingEntity)
    }

    fun checkPaymentStatus(id: Int): QueryStatusTransactionResponse {
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        var response: QueryStatusTransactionResponse? = null
        repeat(5) {
            if (response == null) {
                response = QueryStatusTransaction.process(
                        environment,
                        bookingEntity.orderId.toString(),
                        Calendar.getInstance().timeInMillis.toString()).apply { "QueryStatusTransaction: $response" }
            } else {
                return@repeat
            }
        }
        return response ?: throw MethodNotSuccess()
    }

    fun refundPayment(id: Int): RefundMoMoResponse {
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        if (bookingEntity.orderId == null) throw PaymentRequired()
        val now = Calendar.getInstance().timeInMillis
        val response = RefundMoMo.process(environment, now.toString(), bookingEntity.orderId.toString(), bookingEntity.totalPaid.toString(), "2304963912").apply { log.info { this } }
//        RefundStatus.process(environment, "1562135830002", "1561972787557");
        return response
    }
}

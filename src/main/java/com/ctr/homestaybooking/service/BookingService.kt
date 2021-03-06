package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.FirebaseMessage
import com.ctr.homestaybooking.controller.booking.*
import com.ctr.homestaybooking.entity.BookingEntity
import com.ctr.homestaybooking.repository.BookingRepository
import com.ctr.homestaybooking.shared.*
import com.ctr.homestaybooking.shared.enums.BookingStatus
import com.ctr.homestaybooking.shared.enums.BookingType
import com.ctr.homestaybooking.shared.enums.DateStatus
import com.mservice.allinone.models.CaptureMoMoResponse
import com.mservice.allinone.models.QueryStatusTransactionResponse
import com.mservice.allinone.models.RefundMoMoResponse
import com.mservice.allinone.processor.allinone.CaptureMoMo
import com.mservice.allinone.processor.allinone.QueryStatusTransaction
import com.mservice.allinone.processor.allinone.RefundMoMo
import com.mservice.shared.sharedmodels.Environment
import com.mservice.shared.utils.Encoder
import org.springframework.stereotype.Service
import java.util.*

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */
@Service
class BookingService(private val bookingRepository: BookingRepository,
                     private val placeService: PlaceService,
                     private val userService: UserService,
                     private val fcmService: FcmService
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
                throw MessageException("Start date must be before end date")
            }

            // check booking dates is available
            val bookingDates = startDate.datesUntil(endDate)
            placeService.getPlaceByID(bookingEntity.placeEntity.id).let { place ->
                val unavailableDates = place.bookingSlotEntities?.filter { it.status == DateStatus.UNAVAILABLE }?.map { it.date }
                        ?: listOf()
                if (unavailableDates.isContainAll(bookingDates)) {
                    throw MessageException("Ngày đặt chỗ không có sẵn.")
                }

                // check promo is valid
                bookingEntity.promoEntity?.id?.let { id ->
                    (place.promoEntities?.find { it.id == id }
                            ?: throw MessageException("Promo code is invalid")).apply {

                        if (now.before(startDate)) {
                            throw MessageException("Promo code is not start")
                        }

                        if (now.after(endDate)) {
                            throw MessageException("Promo code is expired")
                        }
                    }
                }
            }

            if (bookingEntity.placeEntity.hostEntity?.id == bookingEntity.userEntity.id) {
                throw MessageException("Can't book your place!")
            }

            return bookingRepository.save(bookingEntity).apply {
                placeEntity.hostEntity?.deviceToken?.let {
                    fcmService.pushNotification(FirebaseMessage(
                            if (placeEntity.bookingType == BookingType.INSTANT_BOOKING) {
                                "Bạn có đơn đặt chỗ mới từ ${userEntity.getName()}"
                            } else {
                                "Bạn có yêu cầu đặt chỗ mới từ ${userEntity.getName()}"
                            },
                            "${placeEntity.name}",
                            it,
                            mapOf("bookingId" to id).toJsonString()
                    ))
                }
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
        return bookingRepository.save(bookingEntity).apply {
            when (bookingStatus) {
                BookingStatus.ACCEPTED -> {
                    userEntity.deviceToken.let {
                        fcmService.pushNotification(FirebaseMessage(
                                "${placeEntity.hostEntity?.getName()} đã chấp nhận yêu cầu đặt chỗ ${this.id}, thanh toán ngay!",
                                "${placeEntity.name}",
                                it,
                                mapOf("bookingId" to id).toJsonString()
                        ))
                    }
                }
                BookingStatus.CANCELLED -> {
                    placeEntity.hostEntity?.deviceToken?.let {
                        fcmService.pushNotification(FirebaseMessage(
                                "Đơn đặt chỗ ${this.id} đã bị hủy",
                                "${placeEntity.name}",
                                it,
                                mapOf("bookingId" to id).toJsonString()
                        ))
                    }
                    userEntity.deviceToken.let {
                        fcmService.pushNotification(FirebaseMessage(
                                "Đơn đặt chỗ ${this.id} đã bị hủy",
                                "${placeEntity.name}",
                                it,
                                mapOf("bookingId" to id).toJsonString()
                        ))
                    }
                }
                else -> {

                }
            }
        }
    }

    fun deleteBookingByID(id: Int) {
        if (bookingRepository.findById(id).toNullable() == null)
            throw BookingNotFoundException(id)
        return bookingRepository.deleteById(id)
    }

    fun requestPayment(id: Int): CaptureMoMoResponse {
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        bookingEntity.apply {
            val bookingDates = startDate.datesUntil(endDate)
            placeEntity.let { place ->
                val unavailableDates = place.bookingSlotEntities?.filter { it.status == DateStatus.UNAVAILABLE }?.map { it.date }
                        ?: listOf()
                if (unavailableDates.isContainAll(bookingDates)) {
                    throw MessageException("Ngày đặt phòng không có sẵn.")
                }
            }
            if (bookingStatus != BookingStatus.ACCEPTED && bookingStatus != BookingStatus.UNPAID) {
                throw MessageException("Booking must be accepted by host")
            }
        }

        val extraData = "merchantName=HomestayBooking"
        val now = Calendar.getInstance().timeInMillis
        bookingEntity.apply {
            var captureMoMoResponse: CaptureMoMoResponse? = null
            repeat(1) {
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
                    bookingEntity.orderId = captureMoMoResponse!!.orderId
                    bookingRepository.save(bookingEntity)
                    return@repeat
                }
            }
            return captureMoMoResponse ?: throw MethodNotSuccess()
        }
    }

    fun changeBookingStatusPaid(id: Int, partnerCode: String, accessKey: String, requestId: String, amount: String, orderId: String, orderInfo: String, orderType: String, transId: String, errorCode: Int, message: String, localMessage: String, payType: String, responseTime: String, extraData: String, signature: String): BookingEntity {
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        if (errorCode == 0) {
            environment.partnerInfo.apply {
                log.info { this }.apply {
                    val raw = "partnerCode=$partnerCode&accessKey=$accessKey&requestId=$requestId\n" +
                            "&amount=$amount&orderId=$orderId&orderInfo=$orderInfo\n" +
                            "&orderType=$orderType&transId=$transId\n" +
                            "&message=$message&localMessage=$localMessage\n" +
                            "&responseTime=$responseTime&errorCode=$errorCode\n" +
                            "&payType=$payType&extraData=$extraData"
                    val sign = Encoder.signHmacSHA256(raw, secretKey)
                    raw.apply { log.info { "raw" + this } }
                    sign.apply { log.info { "sign" + this } }
                    signature.apply { log.info { "raw" + this } }
                }
                bookingEntity.let {
                    it.bookingStatus = BookingStatus.PAID
                    it.orderId = orderId
                    it.transId = transId
                }
            }
        }
        return bookingRepository.save(bookingEntity).apply {
            if (errorCode == 0) {
                val bookingDates = startDate.datesUntil(endDate)
                placeService.updateBookingSlotById(placeEntity.id, bookingDates)
                placeEntity.hostEntity?.deviceToken?.let {
                    fcmService.pushNotification(FirebaseMessage(
                            "${userEntity.getName()} đã thanh toán đơn đặt chỗ ${this.id} ",
                            "${placeEntity.name}",
                            it,
                            mapOf("bookingId" to id).toJsonString()
                    ))
                }
            }
        }
    }

    fun checkPaymentStatus(id: Int): QueryStatusTransactionResponse {
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        var response: QueryStatusTransactionResponse? = null
        repeat(5) {
            if (response == null) {
                response = QueryStatusTransaction.process(
                        environment,
                        bookingEntity.orderId,
                        Calendar.getInstance().timeInMillis.toString()).apply { "QueryStatusTransaction: $response" }
            } else {
                return@repeat
            }
        }
        return response ?: throw MethodNotSuccess()
    }

    fun refundPayment(id: Int): RefundMoMoResponse {
        val bookingEntity = bookingRepository.findById(id).toNullable() ?: throw BookingNotFoundException(id)
        if (bookingEntity.transId == null) throw PaymentRequiredException()
        val now = Calendar.getInstance().timeInMillis
        var response: RefundMoMoResponse? = null
        if (response == null) {
            response = RefundMoMo.process(environment,
                    now.toString(),
                    bookingEntity.orderId.toString(),
                    bookingEntity.totalPaid.toString(),
                    bookingEntity.transId.toString()
            )
        }
//        RefundStatus.process(environment, "1562135830002", "1561972787557");
        return response.apply { log.info { this } } ?: throw MethodNotSuccess()
    }
}

package com.ctr.homestaybooking.controller.booking

import com.ctr.homestaybooking.entity.BookingBody
import com.ctr.homestaybooking.entity.BookingDetail
import com.ctr.homestaybooking.service.BookingService
import com.ctr.homestaybooking.service.PlaceService
import com.ctr.homestaybooking.service.PromoService
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.enums.BookingStatus
import com.ctr.homestaybooking.shared.model.ApiData
import com.mservice.allinone.models.CaptureMoMoResponse
import com.mservice.allinone.models.QueryStatusTransactionResponse
import com.mservice.allinone.models.RefundMoMoResponse
import io.swagger.annotations.ApiOperation
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */

@RestController
@RequestMapping("/api/bookings")
class BookingController(private val bookingService: BookingService,
                        private val placeService: PlaceService,
                        private val userService: UserService,
                        private val promoService: PromoService
) {
    @GetMapping
    @ApiOperation(value = "Get all bookings")
    fun getAllBooking(): ApiData<List<BookingDetail>> = ApiData(bookingService.getAllBooking().map { it.toBookingDetail() })

    @GetMapping("/{id}")
    @ApiOperation(value = "Get booking by id")
    fun getBookingById(@PathVariable("id") id: Int): ApiData<BookingDetail> {
        return ApiData(bookingService.getBookingById(id).toBookingDetail())
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get bookings of user")
    fun getBookingByUserId(@PathVariable("id") id: Int): ApiData<List<BookingDetail>> {
        return ApiData(bookingService.getBookingByUserId(id).map { it.toBookingDetail() })
    }

    @GetMapping("/host/{id}")
    @ApiOperation(value = "Get bookings of host")
    fun getBookingByHostId(@PathVariable("id") id: Int): ApiData<List<BookingDetail>> {
        return ApiData(bookingService.getBookingByHostId(id).map { it.toBookingDetail() })
    }

    @PostMapping
    @ApiOperation(value = "Add a new booking")
    fun addBooking(@RequestBody @Validated bookingBody: BookingBody): ApiData<BookingDetail> {
        return ApiData(bookingService.addBooking(bookingBody.toBookingEntity(placeService, userService, promoService)).toBookingDetail())
    }

    @PutMapping
    @ApiOperation(value = "Update an existing booking")
    fun editBooking(@RequestBody @Validated bookingBody: BookingBody): ApiData<BookingDetail> {
        return ApiData(bookingService.editBooking(bookingBody.toBookingEntity(placeService, userService, promoService)).toBookingDetail())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a booking by id")
    fun deleteBooking(@PathVariable("id") id: Int) {
        bookingService.deleteBookingByID(id)
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Change booking status to PENDING,ACCEPTED,UNPAID,COMPLETED or CANCELLED")
    fun changeBookingStatus(@PathVariable("id") id: Int, bookingStatus: BookingStatus): ApiData<BookingDetail> {
        return ApiData(bookingService.changeBookingStatus(id, bookingStatus).toBookingDetail())
    }

    @PostMapping("/{id}/payment")
    @ApiOperation(value = "Make a request payment to MoMo'server")
    fun requestPayment(@PathVariable("id") id: Int): ApiData<CaptureMoMoResponse?> {
        return ApiData(bookingService.requestPayment(id))
    }

    @PostMapping("/{id}/paid")
    @ApiOperation(value = "MoMo'server change booking status to PAID")
    fun changeBookingStatusPaid(@PathVariable("id") id: Int,
                                @RequestParam("partnerCode") partnerCode: String,
                                @RequestParam("accessKey") accessKey: String,
                                @RequestParam("requestId") requestId: String,
                                @RequestParam("amount") amount: String,
                                @RequestParam("orderId") orderId: String,
                                @RequestParam("orderInfo") orderInfo: String,
                                @RequestParam("orderType") orderType: String,
                                @RequestParam("transId") transId: String,
                                @RequestParam("errorCode") errorCode: Int,
                                @RequestParam("message") message: String,
                                @RequestParam("localMessage") localMessage: String,
                                @RequestParam("payType") payType: String,
                                @RequestParam("responseTime") responseTime: String,
                                @RequestParam("extraData") extraData: String,
                                @RequestParam("signature") signature: String
    ): ApiData<BookingDetail> {
        return ApiData(bookingService.changeBookingStatusPaid(id, partnerCode, accessKey, requestId, amount, orderId, orderInfo, orderType, transId, errorCode, message, localMessage, payType, responseTime, extraData, signature).toBookingDetail())
    }

    @GetMapping("/{id}/payment")
    @ApiOperation(value = "Check a payment status from MoMo'server")
    fun checkPaymentStatus(@PathVariable("id") id: Int): ApiData<QueryStatusTransactionResponse?> {
        return ApiData(bookingService.checkPaymentStatus(id))
    }

    @GetMapping("/{id}/refund")
    @ApiOperation(value = "Refund payment for user")
    fun refundPayment(@PathVariable("id") id: Int): ApiData<RefundMoMoResponse> {
        return ApiData(bookingService.refundPayment(id))
    }
}

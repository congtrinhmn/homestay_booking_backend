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
    @get:GetMapping
    val allBooking: ApiData<List<BookingDetail>>
        get() = ApiData(bookingService.getAllBooking().map { it.toBookingDetail() })

    @GetMapping("/{id}")
    fun getBookingById(@PathVariable("id") id: Int): ApiData<BookingDetail> {
        return ApiData(bookingService.getBookingById(id).toBookingDetail())
    }

    @GetMapping("/user/{id}")
    fun getBookingByUserId(@PathVariable("id") id: Int): ApiData<List<BookingDetail>> {
        return ApiData(bookingService.getBookingByUserId(id).map { it.toBookingDetail() })
    }

    @PostMapping
    fun addBooking(@RequestBody @Validated bookingBody: BookingBody): ApiData<BookingDetail> {
        return ApiData(bookingService.addBooking(bookingBody.toBookingEntity(placeService, userService, promoService)).toBookingDetail())
    }

    @PutMapping
    fun editBooking(@RequestBody @Validated bookingBody: BookingBody): ApiData<BookingDetail> {
        return ApiData(bookingService.editBooking(bookingBody.toBookingEntity(placeService, userService, promoService)).toBookingDetail())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteBooking(@PathVariable("id") id: Int) {
        bookingService.deleteBookingByID(id)
    }

    @PatchMapping("/{id}")
    fun changeBookingStatus(@PathVariable("id") id: Int, bookingStatus: BookingStatus): ApiData<BookingDetail> {
        return ApiData(bookingService.changeBookingStatus(id, bookingStatus).toBookingDetail())
    }

    @PostMapping("/{id}/paid")
    fun changeBookingStatusPaid(@PathVariable("id") id: Int): ApiData<BookingDetail> {
        return ApiData(bookingService.changeBookingStatusPaid(id).toBookingDetail())
    }

    @PostMapping("/{id}/payment")
    fun requestPayment(@PathVariable("id") id: Int): CaptureMoMoResponse {
        return bookingService.requestPayment(id)
    }

    @GetMapping("/{id}/payment")
    fun checkPaymentStatus(@PathVariable("id") id: Int): QueryStatusTransactionResponse {
        return bookingService.checkPaymentStatus(id)
    }
}

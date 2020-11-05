package com.ctr.homestaybooking.controller.booking

import com.ctr.homestaybooking.entity.BookingDetailDto
import com.ctr.homestaybooking.entity.BookingRequest
import com.ctr.homestaybooking.service.BookingService
import com.ctr.homestaybooking.service.PlaceService
import com.ctr.homestaybooking.service.PromoService
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.model.ApiData
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
    val allBooking: ApiData<List<BookingDetailDto>>
        get() = ApiData(bookingService.getAllBooking().map { it.toBookingDetailDto() })

    @GetMapping("/{id}")
    fun getBookingById(@PathVariable("id") id: Int): ApiData<BookingDetailDto> {
        return ApiData(bookingService.getBookingById(id).toBookingDetailDto())
    }

    @PostMapping
    fun addBooking(@RequestBody @Validated bookingRequest: BookingRequest): ApiData<BookingDetailDto> {
        return ApiData(bookingService.addBooking(bookingRequest.toBookingEntity(placeService, userService, promoService)).toBookingDetailDto())
    }

    @PutMapping
    fun editBooking(@RequestBody @Validated bookingRequest: BookingRequest): ApiData<BookingDetailDto> {
        return ApiData(bookingService.editBooking(bookingRequest.toBookingEntity(placeService, userService, promoService)).toBookingDetailDto())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteBooking(@PathVariable("id") id: Int) {
        bookingService.deleteBookingByID(id)
    }
}

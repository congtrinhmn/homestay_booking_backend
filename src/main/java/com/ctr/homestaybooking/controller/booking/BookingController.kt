package com.ctr.homestaybooking.controller.booking

import com.ctr.homestaybooking.entity.BookingDetailResponse
import com.ctr.homestaybooking.entity.BookingEntity
import com.ctr.homestaybooking.entity.BookingRequest
import com.ctr.homestaybooking.service.BookingService
import com.ctr.homestaybooking.service.PlaceService
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
                        private val userService: UserService
) {
    @get:GetMapping
    val allBooking: ApiData<List<BookingDetailResponse>>
        get() = ApiData(bookingService.getAllBooking().map { it.toBookingDetailResponse() })

    @GetMapping("/{id}")
    fun getBookingById(@PathVariable("id") id: Int): ApiData<BookingDetailResponse> {
        return ApiData(bookingService.getBookingById(id).toBookingDetailResponse())
    }

    @PostMapping
    fun addBooking(@RequestBody @Validated bookingRequest: BookingRequest): ApiData<BookingEntity> {
        return ApiData(bookingService.addBooking(bookingRequest.toBookingEntity(placeService, userService)))
    }

    @PutMapping
    fun editBooking(@RequestBody @Validated bookingRequest: BookingRequest): ApiData<BookingEntity> {
        return ApiData(bookingService.editBooking(bookingRequest.toBookingEntity(placeService, userService)))
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteBooking(@PathVariable("id") id: Int) {
        bookingService.deleteBookingByID(id)
    }
}

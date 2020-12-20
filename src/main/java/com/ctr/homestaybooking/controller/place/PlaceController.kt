package com.ctr.homestaybooking.controller.place

import com.ctr.homestaybooking.controller.place.dto.Place
import com.ctr.homestaybooking.controller.place.dto.PlaceBody
import com.ctr.homestaybooking.controller.place.dto.PlaceDetail
import com.ctr.homestaybooking.entity.BookingDetail
import com.ctr.homestaybooking.entity.PlaceEntity
import com.ctr.homestaybooking.service.*
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.ROLE_HOST
import com.ctr.homestaybooking.shared.model.ApiData
import com.ctr.homestaybooking.shared.model.PagingHeaders
import com.ctr.homestaybooking.shared.model.PagingResponse
import io.swagger.annotations.ApiOperation
import net.kaczmarzyk.spring.data.jpa.domain.Equal
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThanOrEqual
import net.kaczmarzyk.spring.data.jpa.domain.LessThanOrEqual
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase
import net.kaczmarzyk.spring.data.jpa.web.annotation.And
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional


/**
 * Created by at-trinhnguyen2 on 2020/10/19
 */

@RestController
@RequestMapping("/api/places")
class PlaceController(private val placeService: PlaceService,
                      private val userService: UserService,
                      private val locationService: LocationService,
                      private val placeTypeService: PlaceTypeService,
                      private val amenityService: AmenityService
) {
    @GetMapping
    @ApiOperation(value = "Get all listed places")
    fun getAllPlace(@RequestParam(defaultValue = "0") page: Int,
                    @RequestParam(defaultValue = "20") size: Int,
                    @RequestParam(defaultValue = "id") sortBy: String
    ): ApiData<List<Place>> {
        return ApiData(placeService.getAllPlace(page, size, sortBy).map { it.toPlace() })
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get place by id")
    fun getPlaceById(@PathVariable("id") id: Int): ApiData<PlaceDetail> {
        return ApiData(placeService.getPlaceByID(id).toPlaceDetail())
    }

    @GetMapping("/ward/{id}")
    @ApiOperation(value = "Get all places in ward")
    fun getPlacesByWardId(@PathVariable("id") id: Int): ApiData<List<Place>> {
        return ApiData(placeService.getPlacesByWardEntity(locationService.getWardById(id)).map { it.toPlace() })
    }

    @GetMapping("/district/{id}")
    @ApiOperation(value = "Get all places in district")
    fun getPlacesByDistrictId(@PathVariable("id") id: Int): ApiData<List<Place>> {
        return ApiData(placeService.getPlacesByDistrictId(id).map { it.toPlace() })
    }

    @GetMapping("/host/{id}")
    @ApiOperation(value = "Get places of host")
    fun getPlacesByHostId(@PathVariable("id") id: Int): ApiData<List<PlaceDetail>> {
        return ApiData(placeService.getPlacesByUserEntity(userService.getUserById(id)).map { it.toPlaceDetail() })
    }

    @PostMapping
    @ApiOperation(value = "Add a new place")
    fun addPlace(@RequestBody @Validated placeBody: PlaceBody): ApiData<PlaceDetail> {
        return ApiData(placeService.addPlace(placeBody.toPlaceEntity(userService, locationService, placeTypeService, amenityService)).toPlaceDetail())
    }

    @PutMapping
    @ApiOperation(value = "Update an existing place")
    fun editPlace(@RequestBody @Validated placeBody: PlaceBody): ApiData<PlaceDetail> {
        return ApiData(placeService.editPlace(placeBody.toPlaceEntity(userService, locationService, placeTypeService, amenityService)).toPlaceDetail())
    }

    @Secured(ROLE_ADMIN, ROLE_HOST)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a place by id")
    fun deletePlace(@PathVariable("id") id: Int): ApiData<PlaceDetail> {
        return ApiData(placeService.deletePlaceByID(id).toPlaceDetail())
    }

    @Secured(ROLE_ADMIN, ROLE_HOST)
    @PatchMapping("/{id}/status")
    @ApiOperation(value = "Reverse place status by id")
    fun reversePlaceStatusByID(@PathVariable("id") id: Int): ApiData<PlaceDetail> {
        return ApiData(placeService.reversePlaceStatusByID(id).toPlaceDetail())
    }

    @GetMapping("/recommend/{id}")
    @ApiOperation(value = "Get recommend places for user")
    fun getRecommendPlacesForUser(@PathVariable("id") id: Int): ApiData<List<Place>> {
        return ApiData(placeService.getRecommendPlaceForUser(id).map { it.toPlace() })
    }

    @GetMapping("/{id}/booking")
    @ApiOperation(value = "Get recommend places for user")
    fun getBookingByPlaceId(@PathVariable("id") id: Int): ApiData<List<BookingDetail>> {
        return ApiData(placeService.getBookingByPlaceId(id).map { it.toBookingDetail() })
    }

    @GetMapping("/{id}/calendar")
    @ApiOperation(value = "Get calendar of place")
    fun getCalendarById(@PathVariable("id") id: Int): ApiData<String> {
        return ApiData(placeService.getCalendarById(id))
    }

    @Transactional
    @GetMapping(value = ["search"], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun get(
            @And(*[Spec(path = "id", params = ["id"], spec = Equal::class),
                Spec(path = "name", params = ["name"], spec = LikeIgnoreCase::class),
                Spec(path = "description", params = ["description"], spec = LikeIgnoreCase::class),
                Spec(path = "address", params = ["address"], spec = LikeIgnoreCase::class),
                Spec(path = "bookingType", params = ["bookingType"], spec = Equal::class),
                Spec(path = "guestCount", params = ["guestCount"], spec = Equal::class),
                Spec(path = "roomCount", params = ["roomCount"], spec = Equal::class),
                Spec(path = "bedCount", params = ["bedCount"], spec = Equal::class),
                Spec(path = "bathroomCount", params = ["bathroomCount"], spec = Equal::class),
                Spec(path = "pricePerDay", params = ["minPrice"], spec = GreaterThanOrEqual::class),
                Spec(path = "pricePerDay", params = ["maxPrice"], spec = LessThanOrEqual::class),
                Spec(path = "cancelType", params = ["cancelType"], spec = Equal::class),
                Spec(path = "status", params = ["status"], spec = Equal::class)
            ])
            spec: Specification<PlaceEntity>?,
            sort: Sort,
            @RequestHeader headers: HttpHeaders
    ): ResponseEntity<List<Place>> {
        return if (spec == null) {
            ResponseEntity(placeService.getAllPlace().map { it.toPlace() }, HttpStatus.OK)
        } else {
            val response: PagingResponse = placeService.get(spec, headers, sort)
            ResponseEntity(response.elements?.map { it.toPlace() }, returnHttpHeaders(response), HttpStatus.OK)
        }
    }

    fun returnHttpHeaders(response: PagingResponse): HttpHeaders? {
        val headers = HttpHeaders()
        headers.set(PagingHeaders.COUNT.pageName, java.lang.String.valueOf(response.count))
        headers.set(PagingHeaders.PAGE_SIZE.pageName, java.lang.String.valueOf(response.pageSize))
        headers.set(PagingHeaders.PAGE_OFFSET.pageName, java.lang.String.valueOf(response.pageOffset))
        headers.set(PagingHeaders.PAGE_NUMBER.pageName, java.lang.String.valueOf(response.pageNumber))
        headers.set(PagingHeaders.PAGE_TOTAL.pageName, java.lang.String.valueOf(response.pageTotal))
        return headers
    }

    @GetMapping("searchPlace")
    fun searchPlace(@RequestParam("text") text: String): ApiData<List<Place>> {
        return ApiData(placeService.searchPlace(text).map { it.toPlace() })
    }
}

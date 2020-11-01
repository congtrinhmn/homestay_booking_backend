package com.ctr.homestaybooking.controller.place

import com.ctr.homestaybooking.controller.place.dto.PlaceDetailResponse
import com.ctr.homestaybooking.controller.place.dto.PlaceRequest
import com.ctr.homestaybooking.controller.place.dto.PlaceResponse
import com.ctr.homestaybooking.service.*
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.ROLE_HOST
import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

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
    @get:GetMapping
    val allPlace: ApiData<List<PlaceResponse>>
        get() = ApiData(placeService.getAllPlace().map { it.toPlaceResponse() })

    @GetMapping("/{id}")
    fun getPlaceById(@PathVariable("id") id: Int): ApiData<PlaceDetailResponse> {
        return ApiData(placeService.getPlaceByID(id).toPlaceDetailResponse())
    }

    @GetMapping("/ward/{id}")
    fun getPlacesByWardId(@PathVariable("id") id: Int): ApiData<List<PlaceResponse>> {
        return ApiData(placeService.getPlacesByWardEntity(locationService.getWardById(id)).map { it.toPlaceResponse() })
    }

    @GetMapping("/district/{id}")
    fun getPlacesByDistrictId(@PathVariable("id") id: Int): ApiData<List<PlaceResponse>> {
        return ApiData(placeService.getPlacesByDistrictId(id).map { it.toPlaceResponse() })
    }

    @PostMapping
    fun addPlace(@RequestBody @Validated placeRequest: PlaceRequest): ApiData<PlaceDetailResponse> {
        return ApiData(placeService.addPlace(placeRequest.toPlaceEntity(userService, locationService, placeTypeService, amenityService)).toPlaceDetailResponse())
    }

    @PutMapping
    fun editPlace(@RequestBody @Validated placeRequest: PlaceRequest): ApiData<PlaceDetailResponse> {
        return ApiData(placeService.editPlace(placeRequest.toPlaceEntity(userService, locationService, placeTypeService, amenityService)).toPlaceDetailResponse())
    }

    @Secured(ROLE_ADMIN, ROLE_HOST)
    @DeleteMapping("/{id}")
    fun deletePlace(@PathVariable("id") id: Int) {
        placeService.deletePlaceByID(id)
    }
}

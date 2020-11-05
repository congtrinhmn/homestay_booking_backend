package com.ctr.homestaybooking.controller.place

import com.ctr.homestaybooking.controller.place.dto.PlaceDetailDto
import com.ctr.homestaybooking.controller.place.dto.PlaceDto
import com.ctr.homestaybooking.controller.place.dto.PlaceRequest
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
    @GetMapping
    fun getAllPlace(@RequestParam(defaultValue = "0") page: Int,
                    @RequestParam(defaultValue = "20") size: Int,
                    @RequestParam(defaultValue = "id") sortBy: String
    ): ApiData<List<PlaceDto>> {
        return ApiData(placeService.getAllPlace(page, size, sortBy).map { it.toPlaceDto() })
    }

    @GetMapping("/{id}")
    fun getPlaceById(@PathVariable("id") id: Int): ApiData<PlaceDetailDto> {
        return ApiData(placeService.getPlaceByID(id).toPlaceDetailDto())
    }

    @GetMapping("/ward/{id}")
    fun getPlacesByWardId(@PathVariable("id") id: Int): ApiData<List<PlaceDto>> {
        return ApiData(placeService.getPlacesByWardEntity(locationService.getWardById(id)).map { it.toPlaceDto() })
    }

    @GetMapping("/district/{id}")
    fun getPlacesByDistrictId(@PathVariable("id") id: Int): ApiData<List<PlaceDto>> {
        return ApiData(placeService.getPlacesByDistrictId(id).map { it.toPlaceDto() })
    }

    @PostMapping
    fun addPlace(@RequestBody @Validated placeRequest: PlaceRequest): ApiData<PlaceDetailDto> {
        return ApiData(placeService.addPlace(placeRequest.toPlaceEntity(userService, locationService, placeTypeService, amenityService)).toPlaceDetailDto())
    }

    @PutMapping
    fun editPlace(@RequestBody @Validated placeRequest: PlaceRequest): ApiData<PlaceDetailDto> {
        return ApiData(placeService.editPlace(placeRequest.toPlaceEntity(userService, locationService, placeTypeService, amenityService)).toPlaceDetailDto())
    }

    @Secured(ROLE_ADMIN, ROLE_HOST)
    @DeleteMapping("/{id}")
    fun deletePlace(@PathVariable("id") id: Int) {
        placeService.deletePlaceByID(id)
    }
}

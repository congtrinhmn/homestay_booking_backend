package com.ctr.homestaybooking.controller.place

import com.ctr.homestaybooking.controller.place.dto.Place
import com.ctr.homestaybooking.controller.place.dto.PlaceBody
import com.ctr.homestaybooking.controller.place.dto.PlaceDetail
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
    ): ApiData<List<Place>> {
        return ApiData(placeService.getAllPlace(page, size, sortBy).map { it.toPlace() })
    }


    @GetMapping("/{id}")
    fun getPlaceById(@PathVariable("id") id: Int): ApiData<PlaceDetail> {
        return ApiData(placeService.getPlaceByID(id).toPlaceDetail())
    }

    @GetMapping("/ward/{id}")
    fun getPlacesByWardId(@PathVariable("id") id: Int): ApiData<List<Place>> {
        return ApiData(placeService.getPlacesByWardEntity(locationService.getWardById(id)).map { it.toPlace() })
    }

    @GetMapping("/district/{id}")
    fun getPlacesByDistrictId(@PathVariable("id") id: Int): ApiData<List<Place>> {
        return ApiData(placeService.getPlacesByDistrictId(id).map { it.toPlace() })
    }

    @GetMapping("/host/{id}")
    fun getPlacesByHostId(@PathVariable("id") id: Int): ApiData<List<PlaceDetail>> {
        return ApiData(placeService.getPlacesByUserEntity(userService.getUserById(id)).map { it.toPlaceDetail() })
    }

    @PostMapping
    fun addPlace(@RequestBody @Validated placeBody: PlaceBody): ApiData<PlaceDetail> {
        return ApiData(placeService.addPlace(placeBody.toPlaceEntity(userService, locationService, placeTypeService, amenityService)).toPlaceDetail())
    }

    @PutMapping
    fun editPlace(@RequestBody @Validated placeBody: PlaceBody): ApiData<PlaceDetail> {
        return ApiData(placeService.editPlace(placeBody.toPlaceEntity(userService, locationService, placeTypeService, amenityService)).toPlaceDetail())
    }

    @Secured(ROLE_ADMIN, ROLE_HOST)
    @DeleteMapping("/{id}")
    fun deletePlace(@PathVariable("id") id: Int) {
        placeService.deletePlaceByID(id)
    }
}

package com.ctr.homestaybooking.controller.location

import com.ctr.homestaybooking.entity.ProvinceDetailDto
import com.ctr.homestaybooking.entity.ProvinceDto
import com.ctr.homestaybooking.service.LocationService
import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by at-trinhnguyen2 on 2020/10/19
 */

@RestController
@RequestMapping("/api/provinces")
class LocationController(private val locationService: LocationService) {
    @get:GetMapping
    val provinces: ApiData<List<ProvinceDto>>
        get() = ApiData(locationService.getProvinces().map { it.toProvinceResponse() })

    @GetMapping("/{id}")
    fun getProvinceById(@PathVariable("id") id: Int): ApiData<ProvinceDetailDto> {
        return ApiData(locationService.getProvinceById(id).toProvinceDetailResponse())
    }
}

package com.ctr.homestaybooking.controller.placetype

import com.ctr.homestaybooking.entity.PlaceTypeEntity
import com.ctr.homestaybooking.service.PlaceTypeService
import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by at-trinhnguyen2 on 2020/10/22
 */
@RestController
@RequestMapping("/api/placeTypes")
class PlaceTypeController(private val placeTypeService: PlaceTypeService
) {
    @get:GetMapping
    val allPlaceType: ApiData<List<PlaceTypeEntity>>
        get() = ApiData(placeTypeService.getAllPlaceType())

    @GetMapping("/{id}")
    fun getPlaceById(@PathVariable("id") id: Int): ApiData<PlaceTypeEntity> {
        return ApiData(placeTypeService.getPlaceTypeById(id))
    }
}

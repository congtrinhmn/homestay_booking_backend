package com.ctr.homestaybooking.controller.promo

import com.ctr.homestaybooking.entity.PromoDetailDto
import com.ctr.homestaybooking.entity.PromoRequest
import com.ctr.homestaybooking.service.PlaceService
import com.ctr.homestaybooking.service.PromoService
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */
@RestController
@RequestMapping("/api/promos")
class PromoController(private val promoService: PromoService,
                      private val placeService: PlaceService
) {
    @get:GetMapping
    val allPromoDetail: ApiData<List<PromoDetailDto>>
        get() = ApiData(promoService.getAllPromo().map { it.toPromoDetailDto() })

    @GetMapping("/{id}")
    fun getPromoById(@PathVariable("id") id: Int): ApiData<PromoDetailDto> {
        return ApiData(promoService.getPromoById(id).toPromoDetailDto())
    }

    @PostMapping
    fun addPromo(@RequestBody @Validated promoRequest: PromoRequest): ApiData<PromoDetailDto> {
        return ApiData(promoService.addPromo(promoRequest.toPromoEntity(placeService)).toPromoDetailDto())
    }

    @PutMapping
    fun editPromo(@RequestBody @Validated promoRequest: PromoRequest): ApiData<PromoDetailDto> {
        return ApiData(promoService.editPromo(promoRequest.toPromoEntity(placeService)).toPromoDetailDto())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deletePromo(@PathVariable("id") id: Int) {
        promoService.deletePromoByID(id)
    }
}

package com.ctr.homestaybooking.controller.promo

import com.ctr.homestaybooking.entity.PromoDetailResponse
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
    val allPromoDetail: ApiData<List<PromoDetailResponse>>
        get() = ApiData(promoService.getAllPromo().map { it.toPromoDetailResponse() })

    @GetMapping("/{id}")
    fun getPromoById(@PathVariable("id") id: Int): ApiData<PromoDetailResponse> {
        return ApiData(promoService.getPromoById(id).toPromoDetailResponse())
    }

    @PostMapping
    fun addPromo(@RequestBody @Validated promoRequest: PromoRequest): ApiData<PromoDetailResponse> {
        return ApiData(promoService.addPromo(promoRequest.toPromoEntity(placeService)).toPromoDetailResponse())
    }

    @PutMapping
    fun editPromo(@RequestBody @Validated promoRequest: PromoRequest): ApiData<PromoDetailResponse> {
        return ApiData(promoService.editPromo(promoRequest.toPromoEntity(placeService)).toPromoDetailResponse())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deletePromo(@PathVariable("id") id: Int) {
        promoService.deletePromoByID(id)
    }
}

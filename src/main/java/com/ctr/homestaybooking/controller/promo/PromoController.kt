package com.ctr.homestaybooking.controller.promo

import com.ctr.homestaybooking.entity.PromoBody
import com.ctr.homestaybooking.entity.PromoDetail
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
    val allPromoDetail: ApiData<List<PromoDetail>>
        get() = ApiData(promoService.getAllPromo().map { it.toPromoDetail() })

    @GetMapping("/{id}")
    fun getPromoById(@PathVariable("id") id: Int): ApiData<PromoDetail> {
        return ApiData(promoService.getPromoById(id).toPromoDetail())
    }

    @PostMapping
    fun addPromo(@RequestBody @Validated promoBody: PromoBody): ApiData<PromoDetail> {
        return ApiData(promoService.addPromo(promoBody.toPromoEntity(placeService)).toPromoDetail())
    }

    @PutMapping
    fun editPromo(@RequestBody @Validated promoBody: PromoBody): ApiData<PromoDetail> {
        return ApiData(promoService.editPromo(promoBody.toPromoEntity(placeService)).toPromoDetail())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deletePromo(@PathVariable("id") id: Int) {
        promoService.deletePromoByID(id)
    }
}

package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.promo.PromoNotFoundException
import com.ctr.homestaybooking.entity.PromoEntity
import com.ctr.homestaybooking.repository.PromoRepository
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.stereotype.Service

/**
 * Created by at-trinhnguyen2 on 2020/10/29
 */
@Service
class PromoService(private val promoRepository: PromoRepository) {

    fun getAllPromo() = promoRepository.findAll().filterIsInstance<PromoEntity>()

    fun getPromoById(id: Int) = promoRepository.findById(id).toNullable()
            ?: throw PromoNotFoundException(id)

    fun addPromo(promoEntity: PromoEntity): PromoEntity {
        return promoRepository.save(promoEntity)
    }

    fun editPromo(promoEntity: PromoEntity): PromoEntity {
        if (promoRepository.findById(promoEntity.id).toNullable() == null)
            throw PromoNotFoundException(promoEntity.id)

        return promoRepository.save(promoEntity)
    }

    fun deletePromoByID(id: Int): PromoEntity {
        val promoEntity: PromoEntity = promoRepository.findById(id).toNullable()
                ?: throw PromoNotFoundException(id)
        promoRepository.delete(promoEntity)
        return promoEntity
    }
}

package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.PlaceEntity
import com.ctr.homestaybooking.entity.WardEntity
import com.ctr.homestaybooking.shared.enums.PlaceStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query

interface PlaceRepository : JpaRepository<PlaceEntity, Int>, JpaSpecificationExecutor<PlaceEntity> {
    fun findByName(name: String): PlaceEntity?

    fun findByStatus(status: PlaceStatus, pageable: Pageable): List<PlaceEntity>

    fun findByWardEntity(wardEntity: WardEntity): Set<PlaceEntity>

    @Query("""select * from places p
            inner join wards w on p.ward_id = w.id
            inner join districts d on w.district_id = d.id
            where d.id = :id""", nativeQuery = true)
    fun findByDistrictId(id: Int): Set<PlaceEntity>?
}

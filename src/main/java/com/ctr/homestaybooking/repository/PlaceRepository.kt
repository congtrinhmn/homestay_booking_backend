package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.PlaceEntity
import com.ctr.homestaybooking.entity.WardEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlaceRepository : JpaRepository<PlaceEntity?, Int?> {
    fun findByName(name: String): PlaceEntity?

    fun findByWardEntity(wardEntity: WardEntity): Set<PlaceEntity>?

    @Query("""select * from places p
            where p.status = LISTED""", nativeQuery = true)
    fun getListedPlaces(): Set<PlaceEntity>?

    @Query("""select * from places p
            inner join wards w on p.ward_id = w.id
            inner join districts d on w.district_id = d.id
            where d.id = :id""", nativeQuery = true)
    fun findByDistrictId(id: Int): Set<PlaceEntity>?
}

package com.ctr.homestaybooking.repository

import com.ctr.homestaybooking.entity.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<ImageEntity?, Int?> {
    fun findByUrl(url: String): ImageEntity?
}

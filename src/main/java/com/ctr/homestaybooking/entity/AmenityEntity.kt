package com.ctr.homestaybooking.entity

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by at-trinhnguyen2 on 2020/10/16
 */
@Entity
@Table(name = "amenities")
class AmenityEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @NotNull
        var name: String
)

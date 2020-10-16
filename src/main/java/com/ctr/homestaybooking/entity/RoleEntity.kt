package com.ctr.homestaybooking.entity

import javax.persistence.*

@Entity
@Table(name = "roles")
data class RoleEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        var name: String? = null)

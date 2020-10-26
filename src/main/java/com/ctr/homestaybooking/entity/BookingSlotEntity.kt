package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.shared.FORMAT_DATE
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * Created by at-trinhnguyen2 on 2020/10/19
 */
@Entity
@Table(name = "booking_slots")
data class BookingSlotEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @Column(nullable = false)
        @Temporal(TemporalType.DATE)
        @DateTimeFormat(pattern = FORMAT_DATE)
        var date: Date,

        @NotNull
        var isAvailable: Boolean = true
)

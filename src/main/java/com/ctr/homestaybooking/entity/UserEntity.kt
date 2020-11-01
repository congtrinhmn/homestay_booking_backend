package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.user.dto.UserDetailResponse
import com.ctr.homestaybooking.shared.FORMAT_DATE
import com.ctr.homestaybooking.shared.enums.Gender
import com.ctr.homestaybooking.shared.enums.UserStatus
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

/**
 * Created by at-trinhnguyen2 on 2020/10/16
 */
@Entity
@Table(name = "users")
class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @Column(nullable = false, unique = true)
        var email: String,

        @Column(nullable = false, unique = true)
        var uuid: String,

        @Column(nullable = false, unique = true)
        var deviceToken: String,

        @Column(nullable = false)
        var password: String,

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "user_role",
                joinColumns = [JoinColumn(name = "user_id")],
                inverseJoinColumns = [JoinColumn(name = "role_id")])
        var roleEntities: Set<RoleEntity>,

        @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
        @JoinColumn(name = "image_id")
        var imageEntity: ImageEntity,

        @NotBlank
        var firstName: String,

        @NotBlank
        var lastName: String,

        @NotBlank
        @Enumerated(EnumType.STRING)
        var gender: Gender,

        @DateTimeFormat(pattern = FORMAT_DATE)
        @Temporal(TemporalType.DATE)
        var birthday: Date,

        @NotBlank
        var phoneNumber: String,

        @Enumerated(EnumType.STRING)
        var status: UserStatus = UserStatus.ACTIVE
) {
    fun toUserDetailResponse(): UserDetailResponse {
        return UserDetailResponse(id, email, uuid, deviceToken, roleEntities, imageEntity.url, firstName, lastName, gender, birthday, phoneNumber)
    }
}


package com.ctr.homestaybooking.entity

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

        @NotBlank
        var imageUrl: String,

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
        fun toUserDetail(): UserDetail {
                return UserDetail(id, email, uuid, deviceToken, roleEntities.map { it.name }.toSet(), imageUrl, firstName, lastName, gender, birthday, phoneNumber)
        }
}

data class UserDetail(
        val id: Int,
        val email: String,
        val uuid: String,
        val deviceToken: String,
        val roles: Set<String>,
        val imageUrl: String,
        val firstName: String,
        val lastName: String,
        var gender: Gender,
        val birthday: Date,
        val phoneNumber: String,
        val status: UserStatus = UserStatus.ACTIVE
)

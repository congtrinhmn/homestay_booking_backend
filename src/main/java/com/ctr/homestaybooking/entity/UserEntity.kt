package com.ctr.homestaybooking.entity

import com.ctr.homestaybooking.controller.user.dto.UserDTO
import com.ctr.homestaybooking.shared.FORMAT_DATE
import com.ctr.homestaybooking.shared.enums.Gender
import com.ctr.homestaybooking.shared.enums.UserStatus
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "users")
data class UserEntity(
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
        var phone_number: String,

        @Enumerated(EnumType.STRING)
        var status: UserStatus = UserStatus.ACTIVE
) {
        fun toUserDto(): UserDTO {
                return UserDTO(id, email, uuid, deviceToken, roleEntities, firstName, lastName, gender, birthday, phone_number)
        }
}

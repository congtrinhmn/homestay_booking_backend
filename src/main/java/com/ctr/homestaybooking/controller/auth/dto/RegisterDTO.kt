package com.ctr.homestaybooking.controller.auth.dto

import com.ctr.homestaybooking.entity.RoleEntity
import com.ctr.homestaybooking.entity.UserEntity
import com.ctr.homestaybooking.shared.*
import com.ctr.homestaybooking.shared.enums.Gender
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class RegisterDTO(
        @field: NotBlank(message = NOT_BLANK_EMAIL)
        @field: Pattern(regexp = PATTERN_EMAIL, message = ERROR_EMAIL)
        val email: String,

        @field: NotBlank(message = NOT_BLANK_PASSWORD)
        @field: Pattern(regexp = PATTERN_PASSWORD, message = ERROR_PASSWORD)
        val password: String,

        @field: NotBlank(message = NOT_BLANK_UUID)
        val uuid: String,

        @field: NotBlank(message = NOT_BLANK_DEVICE_TOKEN)
        val deviceToken: String,

        val roleEntities: Set<RoleEntity>,

        @field: NotBlank(message = NOT_BLANK_FIRST_NAME)
        val firstName: String,

        @field: NotBlank(message = NOT_BLANK_LAST_NAME)
        val lastName: String,

        @NotBlank(message = NOT_BLANK_GENDER)
        @Enumerated(EnumType.STRING)
        var gender: Gender,

        @NotBlank(message = NOT_BLANK_BIRTHDAY)
        @DateTimeFormat(pattern = FORMAT_DATE)
        val birthday: Date,

        @field: NotBlank(message = NOT_BLANK_PHONE_NUMBER)
        @field: Pattern(regexp = PATTERN_PHONE_NUMBER, message = ERROR_PHONE_NUMBER)
        val phone_number: String
) {
        fun toUserEntity(): UserEntity {
                return UserEntity(0, email, uuid, deviceToken, password, roleEntities, firstName, lastName, gender, birthday, phone_number)
        }
}

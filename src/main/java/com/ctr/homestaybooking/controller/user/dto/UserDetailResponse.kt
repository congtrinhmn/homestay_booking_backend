package com.ctr.homestaybooking.controller.user.dto

import com.ctr.homestaybooking.entity.RoleEntity
import com.ctr.homestaybooking.shared.*
import com.ctr.homestaybooking.shared.enums.Gender
import com.ctr.homestaybooking.shared.enums.UserStatus
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

data class UserDetailResponse(
        val id: Int = 0,

        @field: NotBlank(message = NOT_BLANK_EMAIL)
        @field: Pattern(regexp = PATTERN_EMAIL, message = ERROR_EMAIL)
        val email: String,

        @field: NotBlank(message = NOT_BLANK_UUID)
        val uuid: String,

        @field: NotBlank(message = NOT_BLANK_DEVICE_TOKEN)
        val deviceToken: String,

        val roleEntities: Set<RoleEntity>,

        val imageUrl: String,

        @field: NotBlank(message = NOT_BLANK_FIRST_NAME)
        val firstName: String,

        @field: NotBlank(message = NOT_BLANK_LAST_NAME)
        val lastName: String,

        @NotBlank(message = NOT_BLANK_GENDER)
        @Enumerated(EnumType.STRING)
        var gender: Gender,

        @DateTimeFormat(pattern = FORMAT_DATE)
        val birthday: Date,

        @field: NotBlank(message = NOT_BLANK_PHONE_NUMBER)
        @field: Pattern(regexp = "(0)+([0-9]{9})\\b", message = "Phone not in correct format")
        val phoneNumber: String,

        @field: Enumerated(EnumType.STRING)
        val status: UserStatus = UserStatus.ACTIVE
)

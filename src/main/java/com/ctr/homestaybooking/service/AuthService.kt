package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.auth.EmailLoginFailedException
import com.ctr.homestaybooking.controller.auth.InActiveStatusUserException
import com.ctr.homestaybooking.repository.UserRepository
import com.ctr.homestaybooking.shared.enums.UserStatus
import org.springframework.stereotype.Service

@Service
class AuthService(private val userRepository: UserRepository) {

    fun isHandleEmail(email: String): Boolean {
        userRepository.findByEmail(email) ?: throw EmailLoginFailedException()
        return true
    }

    fun isHandleStatus(email: String): Boolean {
        val userEntity = userRepository.findByEmail(email)
        if (userEntity != null) {
            if (userEntity.status == UserStatus.INACTIVE) {
                throw InActiveStatusUserException(email)
            }
        }
        return true
    }
}

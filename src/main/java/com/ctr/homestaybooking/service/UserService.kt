package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.user.exception.UserIsExistsException
import com.ctr.homestaybooking.controller.user.exception.UserIsNotExistsException
import com.ctr.homestaybooking.controller.user.exception.UserNotFoundException
import com.ctr.homestaybooking.entity.RoleEntity
import com.ctr.homestaybooking.entity.UserEntity
import com.ctr.homestaybooking.repository.RoleRepository
import com.ctr.homestaybooking.repository.UserRepository
import com.ctr.homestaybooking.shared.enums.Role
import com.ctr.homestaybooking.shared.enums.UserStatus
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository,
                  private val roleRepository: RoleRepository) {

    val allUser: List<UserEntity> = userRepository.findAll().filterIsInstance<UserEntity>()

    fun getUserById(id: Int): UserEntity {
        return userRepository.findById(id).toNullable()
                ?: throw UserNotFoundException(id)
    }

    fun getUserByEmail(email: String): UserEntity {
        return userRepository.findByEmail(email)
                ?: throw UserNotFoundException(0)
    }

    fun addNewUser(userEntity: UserEntity): UserEntity {
        val userEntityFromDataBase = userRepository.findByEmail(userEntity.email)
        if (userEntityFromDataBase != null) {
            throw UserIsExistsException(userEntityFromDataBase.id)
        }
        if (userEntity.roleEntities.isEmpty()) {
            val roleEntities: Set<RoleEntity> = roleRepository.findByName(Role.ROLE_USER.toString())?.let { setOf(it) }!!
            userEntity.roleEntities = roleEntities
        }
        userEntity.password = BCryptPasswordEncoder().encode(userEntity.password)
        return userRepository.save(userEntity.apply { println("-- addNewUser ${this}") })
    }

    fun editUser(userEntity: UserEntity): UserEntity {
        var user = userEntity
        val userGetFromDatabase = userRepository.findByEmail(user.email)
                ?: throw UserIsNotExistsException(user.id)
        user = holdValueDefault(user, userGetFromDatabase)
        user.password = BCryptPasswordEncoder().encode(user.password)
        userRepository.save(user)
        return userRepository.findById(user.id).get()
    }

    fun deleteUserFollowId(id: Int) {
        if (!userRepository.existsById(id)) {
            throw UserIsNotExistsException(id)
        }
        userRepository.deleteById(id)
    }

    fun reverseStatusUserFollowId(id: Int): UserEntity {
        if (!userRepository.existsById(id)) {
            throw UserIsNotExistsException(id)
        }
        val userEntity = userRepository.findById(id).get()
        userEntity.status = if (userEntity.status == UserStatus.ACTIVE) UserStatus.INACTIVE else UserStatus.ACTIVE
        userRepository.save(userEntity)
        return userEntity
    }

    fun searchUsers(keySearch: String): List<UserEntity> {
        return userRepository.findUsersByKeyword(keySearch)
    }

    fun upToAdminFollowId(id: Int): UserEntity {
        if (!userRepository.existsById(id)) {
            throw UserIsNotExistsException(id)
        }
        val userEntity = userRepository.findById(id).toNullable()!!
        val roleEntity = roleRepository.findByName(Role.ROLE_ADMIN.toString())
        roleEntity?.let {
            userEntity.roleEntities = setOf(it)
        }
        userRepository.save(userEntity)
        return userEntity
    }

    fun holdValueDefault(userChange: UserEntity, user: UserEntity): UserEntity {
        userChange.roleEntities = user.roleEntities
        userChange.id = user.id
        userChange.uuid = user.uuid
        return userChange
    }
}
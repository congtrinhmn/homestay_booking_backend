package com.ctr.homestaybooking.service

import com.ctr.homestaybooking.controller.user.RoleIsNotExistsException
import com.ctr.homestaybooking.controller.user.UserIsExistsException
import com.ctr.homestaybooking.controller.user.UserIsNotExistsException
import com.ctr.homestaybooking.controller.user.UserNotFoundException
import com.ctr.homestaybooking.entity.RoleEntity
import com.ctr.homestaybooking.entity.UserEntity
import com.ctr.homestaybooking.repository.RoleRepository
import com.ctr.homestaybooking.repository.UserRepository
import com.ctr.homestaybooking.shared.enums.Role
import com.ctr.homestaybooking.shared.enums.UserStatus
import com.ctr.homestaybooking.shared.toNullable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository,
                  private val roleRepository: RoleRepository) {

    fun getAllUser(page: Int, size: Int, sortBy: String): List<UserEntity> {
        val paging: Pageable = PageRequest.of(page, size, Sort.by(sortBy))
        return userRepository.findAll(paging).content
    }

    fun getUserById(id: Int): UserEntity {
        return userRepository.findById(id).toNullable() ?: throw UserNotFoundException(id)
    }

    fun getUserByEmail(email: String): UserEntity {
        return userRepository.findByEmail(email)
                ?: throw UserNotFoundException(0)
    }

    fun addUser(userEntity: UserEntity): UserEntity {
        val userEntityFromDataBase = userRepository.findByEmail(userEntity.email)
        if (userEntityFromDataBase != null) {
            throw UserIsExistsException(userEntityFromDataBase.id)
        }
        if (userEntity.roleEntities.isEmpty()) {
            val roleEntities: Set<RoleEntity> = roleRepository.findByName(Role.ROLE_USER.toString())?.let { setOf(it) }!!
            userEntity.roleEntities = roleEntities
        }
        userEntity.password = BCryptPasswordEncoder().encode(userEntity.password)
        return userRepository.save(userEntity)
    }

    fun editUser(userEntity: UserEntity): UserEntity {
        var user = userEntity
        val userGetFromDatabase = userRepository.findByEmail(user.email)
                ?: throw UserIsNotExistsException(user.id)
        user = holdValueDefault(user, userGetFromDatabase)
        user.password = userGetFromDatabase.password
        userRepository.save(user)
        return userRepository.findById(user.id).get()
    }

    fun deleteUserById(id: Int) {
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

    fun search(keySearch: String): List<UserEntity> {
        return userRepository.findUsersByKeyword(keySearch)
    }

    fun upToHostById(id: Int): UserEntity {
        val userEntity = userRepository.findById(id).toNullable() ?: throw UserIsNotExistsException(id)
        val roleEntity = roleRepository.findByName(Role.ROLE_HOST.toString()) ?: throw RoleIsNotExistsException(id)
        userEntity.roleEntities = userEntity.roleEntities.toMutableSet().apply { add(roleEntity) }
        userRepository.save(userEntity)
        return userEntity
    }

    fun upToAdminById(id: Int): UserEntity {
        val userEntity = userRepository.findById(id).toNullable() ?: throw UserIsNotExistsException(id)
        val roleEntity = roleRepository.findByName(Role.ROLE_ADMIN.toString()) ?: throw RoleIsNotExistsException(id)
        userEntity.roleEntities = userEntity.roleEntities.toMutableSet().apply { add(roleEntity) }
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

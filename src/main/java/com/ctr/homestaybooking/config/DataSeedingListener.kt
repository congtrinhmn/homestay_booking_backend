package com.ctr.homestaybooking.config

import com.ctr.homestaybooking.entity.RoleEntity
import com.ctr.homestaybooking.entity.UserEntity
import com.ctr.homestaybooking.repository.RoleRepository
import com.ctr.homestaybooking.repository.UserRepository
import com.ctr.homestaybooking.shared.enums.Gender
import com.ctr.homestaybooking.shared.enums.Role
import com.ctr.homestaybooking.shared.enums.UserStatus
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
@Configuration
class DataSeedingListener(private val userRepository: UserRepository,
                          private val roleRepository: RoleRepository)
    : ApplicationListener<ContextRefreshedEvent?> {

    @Value("\${jwt-key}")
    private val signingKey: String? = null
    private fun addRoleIfMissing(role: Role) {
        if (roleRepository.findByName(role.toString()) == null) {
            val roleEntity = RoleEntity()
            roleEntity.name = role.toString()
            roleRepository.save(roleEntity)
        }
    }

    private fun addUserIfMissing(username: String, password: String, uuid: String, vararg roles: Role) {
        if (userRepository.findByEmail(username) == null) {
            val roleIsExists: MutableSet<RoleEntity> = HashSet()
            for (role in roles) {
                roleRepository.findByName(role.toString())?.let { roleIsExists.add(it) }
            }
            userRepository.save(UserEntity(
                    email = username,
                    password = BCryptPasswordEncoder().encode(password),
                    roleEntities = roleIsExists, firstName = "Trinh",
                    lastName = "Nguyen",
                    birthday = Date(),
                    phone_number = "0123456789",
                    status = UserStatus.ACTIVE,
                    uuid = uuid,
                    deviceToken = uuid,
                    gender = Gender.MALE
            ))
        }
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        addRoleIfMissing(Role.ROLE_ADMIN)
        addRoleIfMissing(Role.ROLE_HOST)
        addRoleIfMissing(Role.ROLE_USER)
        addUserIfMissing("user@gmail.com", "P0p0p0p0", "UUID user", Role.ROLE_USER)
        addUserIfMissing("admin@gmail.com", "P0p0p0p0", "UUID admin", Role.ROLE_USER, Role.ROLE_ADMIN)
        addUserIfMissing("host@gmail.com", "P0p0p0p0", "UUID host", Role.ROLE_USER, Role.ROLE_HOST)
        for (i in 0..14) {
            val username = "admin$i@gmail.com"
            addUserIfMissing(username, "P0p0p0p0", "UUID $i", Role.ROLE_ADMIN, Role.ROLE_USER)
        }
        if (signingKey == null || signingKey.isEmpty()) {
            val jws = Jwts.builder()
                    .setSubject("kunlezIsme")
                    .signWith(SignatureAlgorithm.HS256, "kunlezIsmeApi").compact()
            println("Use this jwt key:")
            println("jwt-key=$jws")
        }
    }
}

package com.ctr.homestaybooking.config

import com.ctr.homestaybooking.entity.*
import com.ctr.homestaybooking.repository.*
import com.ctr.homestaybooking.shared.enums.Amenity
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
import kotlin.collections.HashSet

@Component
@Configuration
class DataSeedingListener(private val userRepository: UserRepository,
                          private val roleRepository: RoleRepository,
                          private val amenityRepository: AmenityRepository,
                          private val placeTypeRepository: PlaceTypeRepository,
                          private val provinceRepository: ProvinceRepository,
                          private val districtRepository: DistrictRepository,
                          private val wardRepository: WardRepository
) : ApplicationListener<ContextRefreshedEvent?> {

    @Value("\${jwt-key}")
    private val signingKey: String? = null

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        Role.values().forEach { addRoleIfMissing(it) }
        addUserIfMissing()
        addProvinceIfMissing()
        Amenity.values().forEach { addAmenityIfMissing(it) }
        placeTypes.forEach { addPlaceTypeIfMissing(it) }

        if (signingKey == null || signingKey.isEmpty()) {
            val jws = Jwts.builder()
                    .setSubject("kunlezIsme")
                    .signWith(SignatureAlgorithm.HS256, "kunlezIsmeApi").compact()
            println("Use this jwt key:")
            println("jwt-key=$jws")
        }
    }

    private val placeTypes = setOf(
            "Homestay",
            "Nhà riêng",
            "Biệt thự",
            "Chung cư",
            "Căn hộ dịch vụ",
            "Khách sạn căn hộ",
            "Khu nghỉ dưỡng",
            "Nhà nghỉ",
            "Nhà trọ",
            "Du thuyền"
    )

    private fun addRoleIfMissing(role: Role) {
        if (roleRepository.findByName(role.toString()) == null) {
            val roleEntity = RoleEntity(name = role.toString())
            roleRepository.save(roleEntity)
        }
    }

    private fun addUserIfMissing() {
        addUserIfMissing("user@gmail.com", "P0p0p0p0", "UUID user", Role.ROLE_USER)
        addUserIfMissing("admin@gmail.com", "P0p0p0p0", "UUID admin", Role.ROLE_USER, Role.ROLE_ADMIN)
        addUserIfMissing("host@gmail.com", "P0p0p0p0", "UUID host", Role.ROLE_USER, Role.ROLE_HOST)
        for (i in 0..14) {
            val username = "admin$i@gmail.com"
            addUserIfMissing(username, "P0p0p0p0", "UUID $i", Role.ROLE_ADMIN, Role.ROLE_USER)
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
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png",
                    lastName = "Nguyen",
                    birthday = Date(),
                    phoneNumber = "0123456789",
                    status = UserStatus.ACTIVE,
                    uuid = uuid,
                    deviceToken = uuid,
                    gender = Gender.MALE
            ))
        }
    }

    private fun addAmenityIfMissing(amenity: Amenity) {
        if (amenityRepository.findByName(amenity.amenityName) == null) {
            amenityRepository.save(AmenityEntity(id = amenity.id, name = amenity.amenityName))
        }
    }

    private fun addPlaceTypeIfMissing(name: String) {
        if (placeTypeRepository.findByName(name) == null) {
            placeTypeRepository.save(PlaceTypeEntity(name = name, description = ""))
        }
    }

    private fun addProvinceIfMissing() {
        if (provinceRepository.findByName("Thành phố Hà Nội") == null) {
            val provinceEntity = provinceRepository.save(ProvinceEntity(1, "Thành phố Trung ương", "Thành phố Hà Nội", null))
            val districtEntity = districtRepository.save((DistrictEntity(1, "Quận", "Quận Ba Đình", null, provinceEntity)))
            wardRepository.save(WardEntity(1, "Phường", "Phường Phúc Xá", districtEntity))
            wardRepository.save(WardEntity(4, "Phường", "Phường Trúc Bạch", districtEntity))
        }
    }

    private fun addWardIfMissing() {
        if (wardRepository.findByName("Phường Phúc Xá") == null) {
            wardRepository.save(
                    WardEntity(1, "Phường", "Phường Phúc Xá",
                            DistrictEntity(1, "Quận", "Quận Ba Đình", null,
                                    ProvinceEntity(1, "Thành phố Trung ương", "Thành phố Hà Nội", null
                                    ))
                    )
            )
        }
    }
}

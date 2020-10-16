package com.ctr.homestaybooking.config

import com.ctr.homestaybooking.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private val userRepository: UserRepository? = null

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val userOpt = userRepository?.findByEmail(username)
        userOpt?.let {
            val grantedAuthorities: MutableSet<GrantedAuthority> = HashSet()
            val roles = userOpt.roleEntities
            for (role in roles) {
                grantedAuthorities.add(SimpleGrantedAuthority(role.name))
            }
            return User(userOpt.email, userOpt.password, grantedAuthorities)
        }
        throw UsernameNotFoundException("User not found")
    }
}

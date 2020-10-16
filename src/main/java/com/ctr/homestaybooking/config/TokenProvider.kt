package com.ctr.homestaybooking.config

import com.ctr.homestaybooking.repository.UserRepository
import com.ctr.homestaybooking.shared.Constants
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

@Component
@Configuration
class TokenProvider {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Value("\${jwt-key}")
    private val signingKey: String? = null
    fun getUsernameFromToken(token: String?): String {
        return getClaimFromToken(token, Function { obj: Claims -> obj.subject })
    }

    fun getExpirationDateFromToken(token: String?): Date {
        return getClaimFromToken(token, Function { obj: Claims -> obj.expiration })
    }

    fun <T> getClaimFromToken(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .body
    }

    private fun isTokenExpired(token: String?): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(authentication: Authentication): String {
        val authorities = authentication.authorities.stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .collect(Collectors.joining(","))
        val userEntity = userRepository.findByEmail(authentication.name)
        return Jwts.builder()
                .setSubject(authentication.name)
                .claim(Constants.AUTHORITIES_KEY, authorities)
                .claim(Constants.KEY_USER_ID, userEntity!!.id)
                .claim(Constants.KEY_USER_UUID, userEntity.uuid)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + Constants.ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun getAuthentication(token: String?, existingAuth: Authentication?, userDetails: UserDetails?): UsernamePasswordAuthenticationToken {
        val jwtParser = Jwts.parser().setSigningKey(signingKey)
        val claimsJws = jwtParser.parseClaimsJws(token)
        val claims = claimsJws.body
        val authorities: Collection<GrantedAuthority> = Arrays.stream(claims[Constants.AUTHORITIES_KEY].toString().split(",".toRegex()).toTypedArray())
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())
        return UsernamePasswordAuthenticationToken(userDetails, "", authorities)
    }
}

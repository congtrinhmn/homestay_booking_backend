package com.ctr.homestaybooking.controller.auth

import com.ctr.homestaybooking.config.TokenProvider
import com.ctr.homestaybooking.controller.auth.dto.AuthToken
import com.ctr.homestaybooking.controller.auth.dto.LoginRequest
import com.ctr.homestaybooking.controller.auth.dto.UserRequest
import com.ctr.homestaybooking.service.AuthService
import com.ctr.homestaybooking.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(private val authenticationManager: AuthenticationManager,
                               private val jwtTokenUtil: TokenProvider,
                               private val userService: UserService,
                               private val authenticationService: AuthService) {

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun register(@RequestBody @Validated userRequest: UserRequest): ResponseEntity<AuthToken> {
        userService.addUser(userRequest.toUserEntity()).toUserDetailDto().apply {
            val authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userRequest.email,
                            userRequest.password
                    )
            )
            SecurityContextHolder.getContext().authentication = authentication
            val token = jwtTokenUtil.generateToken(authentication)
            return ResponseEntity.ok(AuthToken(this, token))
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody @Validated login: LoginRequest): ResponseEntity<AuthToken> {
        if (authenticationService.isHandleEmail(login.email) &&
                authenticationService.isHandleStatus(login.email)) {
            return try {
                val authentication = authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken(
                                login.email,
                                login.password
                        )
                )
                SecurityContextHolder.getContext().authentication = authentication
                val token = jwtTokenUtil.generateToken(authentication)
                ResponseEntity.ok(AuthToken(userService.getUserByEmail(login.email).toUserDetailDto(), token))
            } catch (e: Exception) {
                throw PasswordLoginFailedException()
            }
        }
        throw UsernameNotFoundException("Login failed")
    }

    @PostMapping("/autoLogin")
    fun autoLogin(@RequestBody @Validated token: String): ResponseEntity<AuthToken> {
        val email = jwtTokenUtil.getUsernameFromToken(token)
        val password = ""
        if (authenticationService.isHandleEmail(email) &&
                authenticationService.isHandleStatus(email)) {
            return try {
                val authentication = authenticationManager.authenticate(
                        UsernamePasswordAuthenticationToken(
                                email,
                                password
                        )
                )
                SecurityContextHolder.getContext().authentication = authentication
                ResponseEntity.ok(AuthToken(userService.getUserByEmail(email).toUserDetailDto(), token))
            } catch (e: Exception) {
                throw PasswordLoginFailedException()
            }
        }
        throw UsernameNotFoundException("Login failed")
    }
}

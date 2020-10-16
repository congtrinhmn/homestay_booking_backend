package com.ctr.homestaybooking.controller.auth

import com.ctr.homestaybooking.config.TokenProvider
import com.ctr.homestaybooking.controller.auth.dto.AuthTokenDTO
import com.ctr.homestaybooking.controller.auth.dto.LoginDTO
import com.ctr.homestaybooking.controller.auth.dto.RegisterDTO
import com.ctr.homestaybooking.controller.auth.exception.PasswordLoginFailedException
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
    fun register(@RequestBody @Validated registerDTO: RegisterDTO): ResponseEntity<AuthTokenDTO> {
        userService.addNewUser(registerDTO.toUserEntity()).toUserDto().apply {
            val authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            registerDTO.email,
                            registerDTO.password
                    )
            )
            SecurityContextHolder.getContext().authentication = authentication
            val token = jwtTokenUtil.generateToken(authentication)
            return ResponseEntity.ok(AuthTokenDTO(this, token))
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody @Validated login: LoginDTO): ResponseEntity<AuthTokenDTO> {
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
                ResponseEntity.ok(AuthTokenDTO(userService.getUserByEmail(login.email).toUserDto(), token))
            } catch (e: Exception) {
                throw PasswordLoginFailedException()
            }
        }
        throw UsernameNotFoundException("Login failed")
    }

    @PostMapping("/autoLogin")
    fun login(@RequestBody @Validated token: String): ResponseEntity<AuthTokenDTO> {
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
                ResponseEntity.ok(AuthTokenDTO(userService.getUserByEmail(email).toUserDto(), token))
            } catch (e: Exception) {
                throw PasswordLoginFailedException()
            }
        }
        throw UsernameNotFoundException("Login failed")
    }
}

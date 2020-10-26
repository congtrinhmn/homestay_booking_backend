package com.ctr.homestaybooking.controller.user

import com.ctr.homestaybooking.controller.auth.dto.RegisterDTO
import com.ctr.homestaybooking.controller.user.dto.UserDto
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Int): ApiData<UserDto> {
        return ApiData(userService.getUserById(id).toUserDto())
    }

    @get:GetMapping
    val allUser: ApiData<List<UserDto>>
        get() = ApiData(userService.getAllUser().map { it.toUserDto() })

    @PutMapping
    fun editUser(@RequestBody @Validated registerDTO: RegisterDTO): ApiData<UserDto> {
        return ApiData(userService.editUser(registerDTO.toUserEntity()).toUserDto())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable("id") id: Int) {
        userService.deleteUserById(id)
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable("id") id: Int): ApiData<UserDto> {
        return ApiData(userService.reverseStatusUserFollowId(id).toUserDto())
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("/search")
    fun searchUsers(@RequestParam(name = "valueSearch") valueSearch: String): ApiData<List<UserDto>> {
        return ApiData(userService.search(valueSearch).map { it.toUserDto() })
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/admin")
    fun upToAdmin(@PathVariable("id") id: Int): ApiData<UserDto> {
        return ApiData(userService.upToAdminById(id).toUserDto())
    }

    @PatchMapping("/{id}/host")
    fun upToHost(@PathVariable("id") id: Int): ApiData<UserDto> {
        return ApiData(userService.upToHostById(id).toUserDto())
    }
}

package com.ctr.homestaybooking.controller.user

import com.ctr.homestaybooking.controller.auth.dto.RegisterDTO
import com.ctr.homestaybooking.controller.user.dto.UserDTO
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping("/{id}")
    fun getUserFollowId(@PathVariable("id") id: Int): ApiData<UserDTO> {
        return ApiData(userService.getUserById(id).toUserDto())
    }

    @get:GetMapping
    val allUser: ApiData<List<UserDTO>>
        get() = ApiData(userService.allUser.map { it.toUserDto() })

    @PutMapping
    fun editUser(@RequestBody @Validated registerDTO: RegisterDTO): ApiData<UserDTO> {
        return ApiData(userService.editUser(registerDTO.toUserEntity()).toUserDto())
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable("id") id: Int) {
        userService.deleteUserFollowId(id)
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable("id") id: Int): ApiData<UserDTO> {
        return ApiData(userService.reverseStatusUserFollowId(id).toUserDto())
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/search")
    fun searchUsers(@RequestParam(name = "valueSearch") valueSearch: String): ApiData<List<UserDTO>> {
        return ApiData(userService.searchUsers(valueSearch).map { it.toUserDto() })
    }

    @Secured("ROLE_ADMIN")
    @PatchMapping("/{id}/admin")
    fun upToAdmin(@PathVariable("id") id: Int): ApiData<UserDTO> {
        return ApiData(userService.upToAdminFollowId(id).toUserDto())
    }
}

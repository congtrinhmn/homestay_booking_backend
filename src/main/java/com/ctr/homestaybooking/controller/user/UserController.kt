package com.ctr.homestaybooking.controller.user

import com.ctr.homestaybooking.controller.auth.dto.UserRequest
import com.ctr.homestaybooking.entity.UserDetailDto
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.model.ApiData
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUser(@RequestParam(defaultValue = "0") page: Int,
                   @RequestParam(defaultValue = "20") size: Int,
                   @RequestParam(defaultValue = "id") sortBy: String
    ): ApiData<List<UserDetailDto>> {
        return ApiData(userService.getAllUser(page, size, sortBy).map { it.toUserDetailDto() })
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Int): ApiData<UserDetailDto> {
        return ApiData(userService.getUserById(id).toUserDetailDto())
    }

    @PutMapping
    fun editUser(@RequestBody @Validated userRequest: UserRequest): ApiData<UserDetailDto> {
        return ApiData(userService.editUser(userRequest.toUserEntity()).toUserDetailDto())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable("id") id: Int) {
        userService.deleteUserById(id)
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable("id") id: Int): ApiData<UserDetailDto> {
        return ApiData(userService.reverseStatusUserFollowId(id).toUserDetailDto())
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("/search")
    fun searchUsers(@RequestParam(name = "valueSearch") valueSearch: String): ApiData<List<UserDetailDto>> {
        return ApiData(userService.search(valueSearch).map { it.toUserDetailDto() })
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/admin")
    fun upToAdmin(@PathVariable("id") id: Int): ApiData<UserDetailDto> {
        return ApiData(userService.upToAdminById(id).toUserDetailDto())
    }

    @PatchMapping("/{id}/host")
    fun upToHost(@PathVariable("id") id: Int): ApiData<UserDetailDto> {
        return ApiData(userService.upToHostById(id).toUserDetailDto())
    }
}

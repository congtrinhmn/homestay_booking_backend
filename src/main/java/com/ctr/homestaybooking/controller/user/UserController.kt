package com.ctr.homestaybooking.controller.user

import com.ctr.homestaybooking.controller.auth.dto.UserRequest
import com.ctr.homestaybooking.entity.UserDetail
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
    ): ApiData<List<UserDetail>> {
        return ApiData(userService.getAllUser(page, size, sortBy).map { it.toUserDetail() })
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Int): ApiData<UserDetail> {
        return ApiData(userService.getUserById(id).toUserDetail())
    }

    @PutMapping
    fun editUser(@RequestBody @Validated userRequest: UserRequest): ApiData<UserDetail> {
        return ApiData(userService.editUser(userRequest.toUserEntity()).toUserDetail())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable("id") id: Int) {
        userService.deleteUserById(id)
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable("id") id: Int): ApiData<UserDetail> {
        return ApiData(userService.reverseStatusUserFollowId(id).toUserDetail())
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("/search")
    fun searchUsers(@RequestParam(name = "valueSearch") valueSearch: String): ApiData<List<UserDetail>> {
        return ApiData(userService.search(valueSearch).map { it.toUserDetail() })
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/admin")
    fun upToAdmin(@PathVariable("id") id: Int): ApiData<UserDetail> {
        return ApiData(userService.upToAdminById(id).toUserDetail())
    }

    @PatchMapping("/{id}/host")
    fun upToHost(@PathVariable("id") id: Int): ApiData<UserDetail> {
        return ApiData(userService.upToHostById(id).toUserDetail())
    }
}

package com.ctr.homestaybooking.controller.user

import com.ctr.homestaybooking.controller.auth.dto.UserRequest
import com.ctr.homestaybooking.controller.user.dto.UserDetailResponse
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
    fun getUserById(@PathVariable("id") id: Int): ApiData<UserDetailResponse> {
        return ApiData(userService.getUserById(id).toUserDetailResponse())
    }

    @get:GetMapping
    val allUser: ApiData<List<UserDetailResponse>>
        get() = ApiData(userService.getAllUser().map { it.toUserDetailResponse() })

    @PutMapping
    fun editUser(@RequestBody @Validated userRequest: UserRequest): ApiData<UserDetailResponse> {
        return ApiData(userService.editUser(userRequest.toUserEntity()).toUserDetailResponse())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable("id") id: Int) {
        userService.deleteUserById(id)
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/status")
    fun changeStatus(@PathVariable("id") id: Int): ApiData<UserDetailResponse> {
        return ApiData(userService.reverseStatusUserFollowId(id).toUserDetailResponse())
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("/search")
    fun searchUsers(@RequestParam(name = "valueSearch") valueSearch: String): ApiData<List<UserDetailResponse>> {
        return ApiData(userService.search(valueSearch).map { it.toUserDetailResponse() })
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/admin")
    fun upToAdmin(@PathVariable("id") id: Int): ApiData<UserDetailResponse> {
        return ApiData(userService.upToAdminById(id).toUserDetailResponse())
    }

    @PatchMapping("/{id}/host")
    fun upToHost(@PathVariable("id") id: Int): ApiData<UserDetailResponse> {
        return ApiData(userService.upToHostById(id).toUserDetailResponse())
    }
}

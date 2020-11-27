package com.ctr.homestaybooking.controller.user

import com.ctr.homestaybooking.controller.auth.dto.UserRequest
import com.ctr.homestaybooking.entity.UserDetail
import com.ctr.homestaybooking.service.UserService
import com.ctr.homestaybooking.shared.ROLE_ADMIN
import com.ctr.homestaybooking.shared.model.ApiData
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.security.access.annotation.Secured
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Api(value = "User")
class UserController(private val userService: UserService) {

    @GetMapping
    @ApiOperation(value = "Get all user")
    fun getAllUser(@RequestParam(defaultValue = "0") page: Int,
                   @RequestParam(defaultValue = "20") size: Int,
                   @RequestParam(defaultValue = "id") sortBy: String
    ): ApiData<List<UserDetail>> {
        return ApiData(userService.getAllUser(page, size, sortBy).map { it.toUserDetail() })
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id")
    fun getUserById(@PathVariable("id") id: Int): ApiData<UserDetail> {
        return ApiData(userService.getUserById(id).toUserDetail())
    }

    @PutMapping
    @ApiOperation(value = "Update an existing user")
    fun editUser(@RequestBody @Validated userRequest: UserRequest): ApiData<UserDetail> {
        return ApiData(userService.editUser(userRequest.toUserEntity()).toUserDetail())
    }

    @Secured(ROLE_ADMIN)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a user by id")
    fun deleteUserById(@PathVariable("id") id: Int) {
        userService.deleteUserById(id)
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/status")
    @ApiOperation(value = "Change user status to ACTIVE or INACTIVE")
    fun changeStatus(@PathVariable("id") id: Int): ApiData<UserDetail> {
        return ApiData(userService.reverseStatusUserFollowId(id).toUserDetail())
    }

    @Secured(ROLE_ADMIN)
    @GetMapping("/search")
    @ApiOperation(value = "Search user")
    fun searchUsers(@RequestParam(name = "valueSearch") valueSearch: String): ApiData<List<UserDetail>> {
        return ApiData(userService.search(valueSearch).map { it.toUserDetail() })
    }

    @Secured(ROLE_ADMIN)
    @PatchMapping("/{id}/admin")
    @ApiOperation(value = "Up user role to admin")
    fun upToAdmin(@PathVariable("id") id: Int): ApiData<UserDetail> {
        return ApiData(userService.upToAdminById(id).toUserDetail())
    }

    @PatchMapping("/{id}/host")
    @ApiOperation(value = "Up user role to host")
    fun upToHost(@PathVariable("id") id: Int): ApiData<UserDetail> {
        return ApiData(userService.upToHostById(id).toUserDetail())
    }
}

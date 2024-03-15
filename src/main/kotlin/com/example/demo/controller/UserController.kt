package com.example.demo.controller

import com.example.demo.entity.User
import com.example.demo.service.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/users/register")
    suspend fun registerUser(@RequestBody request: UserRegistrationRequest) {
        val randomNumber = Random().nextInt(1000, 10000).toString()
        val userName = request.userName + "_" + randomNumber

        logCoroutineName("controller")
        userService.registerUser(userName, request.email)
    }

    @GetMapping("/users/occursException")
    suspend fun occursException() {
        userService.occursException()
    }

    @GetMapping("/users/occursException2")
    suspend fun occursException2() {
        userService.occursException2()
    }

    @PostMapping("/users/register2")
    suspend fun registerUser2(@RequestBody request: UserRegistrationRequest) {
        val randomNumber = Random().nextInt(1000, 10000).toString()
        val userName = request.userName + "_" + randomNumber

        logCoroutineName("controller")
        userService.registerUser2(userName, request.email)
    }

    @GetMapping("/users")
    suspend fun getAllUsers(): List<User> {
        return userService.getAllUsers()
    }

    @PostMapping("/purchase")
    suspend fun purchase(@RequestBody request: UserPurchaseRequest) {
        logCoroutineName("controller start")
        userService.purchase(request)
        logCoroutineName("controller end")
    }

    @PostMapping("/purchase1")
    suspend fun purchase1(@RequestBody request: UserPurchaseRequest) {
        logCoroutineName("controller start")
        userService.purchase1(request)
        logCoroutineName("controller end")
    }

    @GetMapping("/purchaseHistory")
    suspend fun purchaseHistory(@RequestParam userId: String) {
        userService.purchaseHistory(userId)
    }

    suspend fun logCoroutineName(prefix: String) {
        println("$prefix - [${Thread.currentThread().name}]")
    }
}

data class UserRegistrationRequest(val userName: String, val email: String)

data class UserPurchaseRequest(val goodsId: Long, val quantity: Int, val userId: String)
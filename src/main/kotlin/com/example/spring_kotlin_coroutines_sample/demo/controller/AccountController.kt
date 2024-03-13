package com.example.spring_kotlin_coroutines_sample.demo.controller

import com.example.spring_kotlin_coroutines_sample.demo.entity.Account
import com.example.spring_kotlin_coroutines_sample.demo.service.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(private val accountService: AccountService) {

    @GetMapping("/accounts")
    suspend fun getAccounts(): List<Account> {
        return accountService.getAllAccounts()
    }
}
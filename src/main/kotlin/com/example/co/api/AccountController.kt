package com.example.co.api

import com.example.co.application.AccountService
import com.example.co.dto.request.AccountParam
import com.example.co.dto.response.AccountDTO
import org.springframework.web.bind.annotation.*

@RestController
class AccountController(private val accountService: AccountService) {

    @GetMapping("/accounts")
    suspend fun getAccounts(): List<AccountDTO> {
        return accountService.getAllAccounts()
    }

    @PostMapping("/accounts")
    suspend fun saveAccount(@RequestBody accountParam: AccountParam) {
        return accountService.saveAccount(accountParam)
    }

    @PutMapping("/accounts/{accountId}")
    suspend fun updateAccount(@RequestBody accountParam: AccountParam, @PathVariable accountId: Long) {
        return accountService.updateAccount(accountParam, accountId)
    }
}
package com.example.spring_kotlin_coroutines_sample.demo.service

import com.example.spring_kotlin_coroutines_sample.demo.entity.Account
import com.example.spring_kotlin_coroutines_sample.demo.repository.AccountRepository
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service


@Service
class AccountService(private val accountRepository: AccountRepository) {

    suspend fun getAllAccounts(): List<Account> {
        return accountRepository.findAll().toList()
    }
}
package com.example.co.dto.request

import com.example.co.domain.Account

data class AccountParam(
    val name: String,
    val email: String
) {
    fun toEntity(): Account = Account(name = name, email = email)
}
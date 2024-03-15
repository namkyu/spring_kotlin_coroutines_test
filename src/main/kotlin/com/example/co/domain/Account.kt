package com.example.co.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("account")
class Account(
    @Id var id: Long? = null,
    var name: String,
    var email: String
)


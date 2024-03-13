package com.example.spring_kotlin_coroutines_sample.demo.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("account")
class Account(
        @Id var id: Long? = null,
        var name: String,
        var email: String
)

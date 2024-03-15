package com.example.demo.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table


@Table(name = "shop_user")
data class User(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val userName: String,
    val email: String
)
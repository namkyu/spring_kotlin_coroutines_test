package com.example.spring_kotlin_coroutines_sample.demo.repository

import com.example.spring_kotlin_coroutines_sample.demo.entity.Account
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AccountRepository : CoroutineCrudRepository<Account, Long>

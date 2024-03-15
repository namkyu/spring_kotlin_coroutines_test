package com.example.co.infra

import com.example.co.domain.Account
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : CoroutineCrudRepository<Account, Long>
package com.example.co.infra

import com.example.co.domain.Account
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.context.annotation.Import

@DataR2dbcTest
@Import(AccountRepository::class)
class AccountRepositoryTest {

    @Autowired
    private lateinit var repository: AccountRepository

    @Test
    fun `test find by username`() = runTest {
        // given
        val account = Account(id = 1, name = "testUser", email = "test@example.com")
    }
}
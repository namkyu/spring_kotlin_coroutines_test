package com.example.co.api

import com.example.co.dto.request.AccountParam
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForObject

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest2(@Autowired val client: TestRestTemplate) {

    @Test
    fun `save account`() {
        val accountParam = AccountParam(email = "test@example.com", name = "test")
        client.postForObject<AccountParam>("/accounts", accountParam)

        client.getForEntity("/accounts", List::class.java).also {
            assert(it.statusCode.is2xxSuccessful)
            assert(it.body != null)
            println(it.body)
        }
    }
}
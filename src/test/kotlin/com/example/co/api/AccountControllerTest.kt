package com.example.co.api

import com.example.co.application.AccountService
import com.example.co.dto.response.AccountDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(
    controllers = [AccountController::class]
)
class AccountControllerTest(@Autowired val mockMvc: MockMvc) {

    // lateinit 키워드는 해당 프로퍼티를 나중에 초기화 한다.
    @MockkBean
    private lateinit var accountService: AccountService

    @Test
    fun `find messages`() = runTest {
        val accounts = listOf(
            AccountDTO(name = "test", email = "test@example.com"),
            AccountDTO(name = "test2", email = "test2@example.com")
        )

        // 코루틴을 지원하는 메서드에 대한 결과 설정
        coEvery { accountService.getAllAccounts() } returns accounts

        mockMvc.get("/accounts")
            .asyncDispatch() // 비동기 요청의 결과를 가져온다.
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
            .andDo { print() }

        coVerify(exactly = 1) { accountService.getAllAccounts() }
    }
}
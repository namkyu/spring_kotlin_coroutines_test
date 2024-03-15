package com.example.co.common.config

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class WebClientTest {

    @LocalServerPort
    private lateinit var port: Integer

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun testGetRequest() {
        // Mock 서버 응답 설정
        webTestClient.get()
            .uri("/demo/hello")
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java).isEqualTo("Hello, World!")
    }

    @Test
    fun testTimeout() {
        assertThrows<IllegalStateException> {
            webTestClient.get()
                .uri("/demo/timeout")
                .accept(MediaType.TEXT_PLAIN)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.GATEWAY_TIMEOUT)
        }
    }
}
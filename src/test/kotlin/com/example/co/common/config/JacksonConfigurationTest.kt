package com.example.co.common.config

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JacksonConfigurationTest {

    @Test
    fun objectMapperTest() {
        // given
        val objectMapper = JacksonConfiguration().objectMapper()

        // when
        val result = objectMapper.writeValueAsString(mapOf("name" to "test")) // 키 to 값

        // then
        assertEquals("{\"name\":\"test\"}", result)
    }
}

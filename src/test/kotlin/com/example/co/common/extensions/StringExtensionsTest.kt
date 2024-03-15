package com.example.co.common.extensions

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StringExtensionsTest {

    @Test
    fun `문자열 뒤에 느낌표 추가`() {
        val result = "hello".addExclamation()
        assertEquals("hello!", result)
    }

    @Test
    fun `숫자를 킬로미터 단위로 변환`() {
        val result = 1000.convertToMileage()
        assertEquals("1,000km", result)
    }
}
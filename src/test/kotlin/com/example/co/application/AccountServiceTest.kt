package com.example.co.application

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AccountServiceTest {


    @Test
    fun `비동기 결과 값 toList()로 변환처리`() = runTest {
        val numbers = flow {
            emit(1)
            emit(2)
            emit(3)
        }

        val list = numbers.toList()
        assertEquals(listOf(1, 2, 3), list)
    }
}
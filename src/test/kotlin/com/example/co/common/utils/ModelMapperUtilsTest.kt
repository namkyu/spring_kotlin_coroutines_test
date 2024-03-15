package com.example.co.common.utils

import com.example.co.domain.Account
import com.example.co.dto.response.AccountDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ModelMapperUtilsTest {

    @Test
    fun mapTest() {
        val result = ModelMapperUtils.map("test", String::class.java)
        assertEquals("test", result)
    }

    @Test
    fun mapToObjectTest() {
        val hMap = HashMap<String, String>()
        hMap["test1"] = "a"
        hMap["test2"] = "b"
        hMap["test3"] = "c"

        val benReq = ModelMapperUtils.map(hMap, BeanReq::class.java)
        assertEquals("a", benReq.test1)
    }

    @Test
    fun mapToListTest() {
        val accountList = listOf(
            Account(1, "test1", "test1@example.com"),
            Account(2, "test2", "test2@example.com")
        )

        val newList = ModelMapperUtils.mapToList<Account, AccountDTO>(accountList)
        assertEquals(2, newList.size)
        assertEquals("test2", newList[1].name)
    }
}

class BeanReq {
    var test1: String? = null
    var test2: String? = null
    var test3: String? = null
}
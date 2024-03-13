package com.example.spring_kotlin_coroutines_sample.demo.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class AccountControllerTest {

    @Test
    fun test1() = runBlocking {
        println("Hello, ${Thread.currentThread().name} thread!")
        launch {
            println("Goodbye, ${Thread.currentThread().name} thread!")
        }
        println("Wait, ${Thread.currentThread().name} thread!")
    }

    @Test
    fun test2() = runBlocking {
        launch {
            println("launch A Start")
            println("launch A End")
        }
        launch {
            println("launch B Start")
            println("launch B End")
        }
        println("Hello World!")
    }

    @Test
    fun test3() = runBlocking {
        launch {
            println("launch A Start")
            delay(1000L)
            println("launch A End")
        }
        launch {
            println("launch B Start")
            println("launch B End")
        }
        println("Hello World!")
    }
}



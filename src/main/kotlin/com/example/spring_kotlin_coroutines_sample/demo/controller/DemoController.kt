package com.example.spring_kotlin_coroutines_sample.demo.controller

import com.example.spring_kotlin_coroutines_sample.demo.service.DemoService
import kotlinx.coroutines.delay
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController(private val demoService: DemoService) {

    @GetMapping("/test")
    fun test() {
        println("controller - [${Thread.currentThread().name}]")
    }

    @GetMapping("/test1")
    suspend fun test1() {
        logCoroutineName()
    }

    @GetMapping("/test2")
    suspend fun test2(): Long {
        logCoroutineName()
        return demoService.test2()
    }

    @GetMapping("/test3")
    suspend fun test3(): Long {
        logCoroutineName()
        return demoService.test3()
    }

    @GetMapping("/test4")
    suspend fun test4(): Long {
        logCoroutineName()
        return demoService.test4()
    }

    @GetMapping("/process2")
    suspend fun processRequest2(): Long {
        logCoroutineName()
        return demoService.processRequest2()
    }

    @GetMapping("/process3")
    suspend fun processRequest3(): Long {
        logCoroutineName()
        return demoService.processRequest3()
    }

    suspend fun logCoroutineName() {
        println("controller - [${Thread.currentThread().name}]")
    }
}
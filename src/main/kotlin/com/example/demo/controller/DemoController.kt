package com.example.demo.controller

import com.example.demo.service.DemoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class DemoController(private val demoService: DemoService) {

    @GetMapping("/hello")
    fun test(): String {
        println("controller - [${Thread.currentThread().name}]")
        return "Hello, World!"
    }

    @GetMapping("/timeout")
    suspend fun timeout() {
        Thread.sleep(5500)
    }

    @GetMapping("/test1")
    suspend fun test1() {
        println("controller - [${Thread.currentThread().name}]")
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

    @GetMapping("/test5")
    suspend fun test5(): Long {
        logCoroutineName()
        return demoService.test5()
    }

    @GetMapping("/test6")
    suspend fun test6(): Long {
        logCoroutineName()
        return demoService.test6()
    }

    @GetMapping("/test7")
    suspend fun test7(): Long {
        logCoroutineName()
        return demoService.test7()
    }

    @GetMapping("/test8")
    suspend fun test8(): Long {
        logCoroutineName()
        return demoService.test8()
    }

    @GetMapping("/test88")
    suspend fun test88(): Long {
        logCoroutineName()
        return demoService.test88()
    }

    @GetMapping("/test9")
    suspend fun test9(): Long {
        logCoroutineName()
        return demoService.test9()
    }

    suspend fun logCoroutineName() {
        println("controller - [${Thread.currentThread().name}]")
    }
}
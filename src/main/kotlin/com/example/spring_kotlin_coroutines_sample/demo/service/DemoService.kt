package com.example.spring_kotlin_coroutines_sample.demo.service

import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis


@Service
class DemoService {

    private val delayTime = 3000L

    suspend fun test2(): Long {
        logCoroutineName()
        return 100L
    }

    suspend fun test3(): Long {
        logCoroutineName()
        return measureTimeMillis {
            logCoroutineName()
            callAPI1()
        }
    }

    suspend fun test4(): Long {
        logCoroutineName()
        return measureTimeMillis {
            logCoroutineName()
            callAPI1()
            callAPI2()
        }
    }

    suspend fun processRequest(): Long {
        var executionTime: Long
        withContext(coroutineContext) {
            executionTime = measureTimeMillis {
                val a = callAPI1()
                val b = callAPI2()
                val c = callAPI3()
                println(a + b + c)
            }
        }
        return executionTime
    }



    suspend fun processRequest2(): Long {
        var executionTime: Long
        logCoroutineName()
        withContext(coroutineContext) {
            logCoroutineName()
            executionTime = measureTimeMillis {
                val deferredA = async { callAPI1() }
                val deferredB = async { callAPI2() }
                val deferredC = async { callAPI3() }
                val results = awaitAll(deferredA, deferredB, deferredC)
                println(results.sumOf { it })
            }
        }
        return executionTime
    }

    suspend fun processRequest3(): Long {
        var executionTime: Long
        withContext(coroutineContext) { // 코루틴 컨텍스트 지정
            executionTime = measureTimeMillis {
                launch { callAPI1() }
                launch { callAPI2() }
                launch { callAPI3() }
            }
        }
        return executionTime
    }

    suspend fun callAPI1(): Int {
        logCoroutineName()
        delay(delayTime) // 코루틴이 제공하는 함수로 넌블록킹
        return 1
    }

    suspend fun callAPI2(): Int {
        logCoroutineName()
        delay(delayTime)
        return 2
    }

    suspend fun callAPI3(): Int {
        logCoroutineName()
        delay(delayTime)
        return 3
    }

    suspend fun logCoroutineName() {
        println("service - [${Thread.currentThread().name}]")
    }
}
package com.example.demo.service

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

        val resultTime = measureTimeMillis {
            logCoroutineName()
            callAPI1()
        }

        println("수행시간 : ${resultTime}")
        return resultTime
    }

    suspend fun test4(): Long {
        logCoroutineName()

        val resultTime = measureTimeMillis {
            logCoroutineName()
            callAPI1()
            callAPI2()
        }

        println("수행시간 : ${resultTime}")
        return resultTime
    }

    suspend fun test5(): Long {
        logCoroutineName()

        val resultTime = measureTimeMillis {
            logCoroutineName()
            callAPI1()
            callAPI2()
            callAPI3()
        }

        println("수행시간 : ${resultTime}")
        return resultTime
    }

    suspend fun test6(): Long {
        logCoroutineName()
        var executionTime: Long

        withContext(coroutineContext) {
            logCoroutineName()
            executionTime = measureTimeMillis {
                callAPI1()
            }
        }

        println("수행시간 : ${executionTime}")
        return executionTime
    }

    suspend fun test7(): Long {
        logCoroutineName()
        var executionTime: Long

        withContext(coroutineContext) {
            logCoroutineName()
            executionTime = measureTimeMillis {
                callAPI1()
                callAPI2()
            }
        }

        println("수행시간 : ${executionTime}")
        return executionTime
    }

    suspend fun test8(): Long {
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

        println("수행시간 : ${executionTime}")
        return executionTime
    }

    suspend fun test88(): Long {
        var executionTime: Long
        logCoroutineName()

        withContext(coroutineContext) {
            logCoroutineName()
            executionTime = measureTimeMillis {
                val deferredA = test88CallAPI1()
                val deferredB = test88CallAPI2()
                val deferredC = test88CallAPI3()
                val results = awaitAll(deferredA, deferredB, deferredC)
                println(results.sumOf { it })
            }
        }

        println("수행시간 : ${executionTime}")
        return executionTime
    }

    suspend fun test88CallAPI1(): Deferred<Int> = coroutineScope {
        async {
            logCoroutineName()
            delay(delayTime)
            1
        }
    }

    suspend fun test88CallAPI2(): Deferred<Int> = coroutineScope {
        async {
            logCoroutineName()
            delay(delayTime)
            2
        }
    }

    suspend fun test88CallAPI3(): Deferred<Int> = coroutineScope {
        async {
            logCoroutineName()
            delay(delayTime)
            3
        }
    }

    suspend fun test9(): Long {
        logCoroutineName()
        var executionTime: Long

        withContext(coroutineContext) { // 코루틴 컨텍스트 지정
            logCoroutineName()
            executionTime = measureTimeMillis {
                launch { callAPI1() }
                launch { callAPI2() }
                launch { callAPI3() }
            }
        }

        println("수행시간 : ${executionTime}")
        return executionTime
    }

    suspend fun callAPI1(): Int {
        logCoroutineName()
        delay(delayTime)
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
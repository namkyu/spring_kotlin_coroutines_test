package com.example.study

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class CoroutineAsyncTest {

    @Test
    fun test_async_await() = runBlocking {
        logCoroutineName()
        // deferred 객체는 미래의 어느 시점에 결과값이 반환될 수 있음을 표현하는 코루틴 객체
        // await 함수를 호출한 코루틴을 일시 중단하며 deffered 코루틴이 실행 완료되면 결과값을 반환하고 호출부의 코루틴을 재개한다. (job join() 함수와 유사하게 동작)
        val networkDeferred = async(Dispatchers.IO) {
            logCoroutineName()
            delay(1000L)
            return@async "dummy response"
        }

        val result = networkDeferred.await() // networkDeferred로부터 결과값이 반환될때까지 runBlocking 일시 중단
        println(result)
    }

    @Test
    fun test_async_await_all() = runBlocking {
        logCoroutineName()

        val executionTime = measureTimeMillis {
            val networkDeferred = async(Dispatchers.IO) {
                logCoroutineName()
                delay(1000L)
                return@async arrayOf("a", "b")
            }

            val networkDeferred2 = async(Dispatchers.IO) {
                logCoroutineName()
                delay(1000L)
                return@async arrayOf("c")
            }

            val results: List<Array<String>> = awaitAll(networkDeferred, networkDeferred2) // 요청이 끝날 때까지 대기
            println("${listOf(*results[0], *results[1])}")
        }

        println("${executionTime}ms 소요됨")
    }

    private fun logCoroutineName() {
        println(Thread.currentThread().name)
    }
}
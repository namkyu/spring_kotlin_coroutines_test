package com.example.study

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class StudyTest {

    @Test
    fun test1() = runBlocking {
        repeat(100_000) {
            launch {
                delay(5000L)
                print(".")
            }
        }
    }

    @Test
    fun test2() {
        repeat(100_000) {
            thread {
                Thread.sleep(1000L)
                print(".")
            }
        }
    }

    @Test
    fun suspendTest1() = runBlocking {
        println("${Thread.currentThread().name} thread!")
        suspendFunction()
    }

    private suspend fun suspendFunction() {
        println("${Thread.currentThread().name} thread!")
        val job1 = CoroutineScope(Dispatchers.IO).async {
            println("job1 ${Thread.currentThread().name} thread!")
            delay(1000L)
            200
        }

        val job2 = CoroutineScope(Dispatchers.Default).async {
            println("job2 ${Thread.currentThread().name} thread!")
            delay(1000L)
            100
        }

        val result = awaitAll(job1, job2)
        result.forEach {
            println(it)
        }
    }

    @Test
    fun futureTest() {
        val startTime = System.currentTimeMillis()
        val executor = Executors.newFixedThreadPool(2)

        val completableFuture = CompletableFuture.supplyAsync({
            Thread.sleep(1000L)
            return@supplyAsync "결과"
        }, executor)

        completableFuture.thenAccept { // thenAccept는 스레드를 블록킹 하지 않음
            println(it)
        }

        println("다른 작업 실행")
        executor.shutdown()
    }
}
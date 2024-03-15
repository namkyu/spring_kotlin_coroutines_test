package com.example.study

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class CoroutineErrorTest {

    // runBlocking - Coroutine1 - Coroutine3
    //             - Coroutine2
    // Coroutine3 에서 예외가 발생하고, 예외가 Coroutine1로 전파, 예외를 처리하지 않았기 때문에 runBlocking으로 전파
    // runBlocking 코루틴이 취소되면서 자식 Coroutine2도 함께 취소 처리
    @Test
    fun test_error1() = runBlocking {
        launch(CoroutineName("Coroutine1")) {
            launch(CoroutineName("Coroutine3")) {
                delay(300L)
                throw Exception("예외 발생")
            }

            delay(100L)
            logCoroutineName("코루틴1 실행")
        }

        launch(CoroutineName("Coroutine2")) {
            delay(500L)
            logCoroutineName("코루틴2 실행")
        }

        delay(1000L)
    }

    // try catch 한다고 해서 코루틴이 취소되는 것을 막을 수 없다.
    @Test
    fun test_error2() = runBlocking {
        launch(CoroutineName("Coroutine1")) {
            try {
                launch(CoroutineName("Coroutine3")) {
                    delay(300L)
                    throw Exception("예외 발생")
                }
            } catch (e: Throwable) {
                println("Exception 발생")
            }

            delay(100L)
            logCoroutineName("코루틴1 실행")
        }

        launch(CoroutineName("Coroutine2")) {
            delay(500L)
            logCoroutineName("코루틴2 실행")
        }

        delay(1000L)
    }

    // SupervisorJob은 하위 코루틴의 모든 예외를 무시하는 특별한 종류의 작업
    @Test
    fun test_error3() = runBlocking {
        val scope = CoroutineScope(SupervisorJob())

        scope.launch(CoroutineName("Coroutine1")) {
            scope.launch(CoroutineName("Coroutine3")) {
                delay(300L)
                throw Exception("예외 발생")
            }

            delay(100L)
            logCoroutineName("Coroutine1 실행")
        }

        scope.launch(CoroutineName("Coroutine2")) {
            delay(500L)
            logCoroutineName("Coroutine2 실행")
        }


        delay(1000L)
        logCoroutineName("End")
    }

    private suspend fun logCoroutineName(prefix: String) {
        println("$prefix - ${Thread.currentThread().name}")
    }

    private suspend fun logCoroutineName() {
        println(Thread.currentThread().name)
    }
}
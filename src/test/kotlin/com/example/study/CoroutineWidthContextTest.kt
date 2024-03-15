package com.example.study

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class CoroutineWidthContextTest {

    // withContext 함수를 사용하면 async-await 작업을 대체할 수 있다.
    // withContext 함수로 대체되면 deffered 객체가 생성되는 부분이 없어지고 결과값을 바로 반환받을 수 있다. async-await 쌍을 깔끔하게 만들 수 있다.
    // async-await 새로운 코루틴을 생성해 작업을 처리하지만 withContext 함수는 실행 중이던 코루틴을 그대로 유지한 채로 코루틴의 실행 환경만 변경해 작업을 처리한다.
    // withContext 함수는 새로운 코루틴을 만들지 않기 때문에 하나의 코루틴에서 withContext 함수를 여러 번 호출되면 순차적으로 실행된다. 성능 저하 문제 발생
    // withContext 함수를 사용하면 코드가 깔끔해 보이는 효과를 내지만 잘못 사용하게 되면 코루틴을 동기적으로 실행하도록 만든다.
    // withContext 함수는 새로운 코루틴을 만들지 않는다. (중요!)
    @Test
    fun test_withcontext() = runBlocking {
        logCoroutineName()

        val executionTime = measureTimeMillis {
            val networkDeferred = withContext(Dispatchers.IO) {
                logCoroutineName("실행1")
                delay(1000L)
                return@withContext arrayOf("a", "b")
            }

            val networkDeferred2 = withContext(Dispatchers.IO) {
                logCoroutineName("실행2")
                delay(1000L)
                return@withContext arrayOf("c")
            }

            println("${listOf(*networkDeferred, *networkDeferred2)}")
        }

        println("${executionTime}ms 소요됨")
    }

    private fun logCoroutineName(prefix: String) {
        println("$prefix - ${Thread.currentThread().name}")
    }

    private fun logCoroutineName() {
        println(Thread.currentThread().name)
    }
}
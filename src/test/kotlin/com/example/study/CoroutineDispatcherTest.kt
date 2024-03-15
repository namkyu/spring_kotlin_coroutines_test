package com.example.study

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class CoroutineDispatcherTest {

    /**
     * launch 빌더에서 생성되는 코루틴을 dispatcher로 보낸다.
     * CoroutineDispatcher 작업 대기열에 코루틴이 들어간다.
     * 사용 가능한 SingleThread 스레드가 있으므로 코루틴을 전달한다.
     * SingleThread 가 코루틴을 실행한다.
     */
    @Test
    fun test_dispatcher() = runBlocking<Unit> {
        logCoroutineName()

        val dispatcher = newSingleThreadContext(name = "SingleThread")

        launch(context = dispatcher) {
            logCoroutineName()
        }
    }

    /**
     * CoroutineDispatcher 작업 대기열에 코루틴이 두 개 들어간다.
     * 사용 가능한 MultiThread 스레드가 있으므로 코루틴을 전달한다.
     * MultiThread 가 코루틴을 실행한다.
     * launch에서 생성한 코루틴이 어느 스레드에서 실행될지는 그때 그때 다르다.
     */
    @Test
    fun test_multithread_dispatcher() = runBlocking<Unit> {
        logCoroutineName()

        val dispatcher = newFixedThreadPoolContext(2, "MultiThread")

        launch(context = dispatcher) {
            logCoroutineName()
        }

        launch(context = dispatcher) {
            logCoroutineName()
        }
    }

    /**
     * 개발자가 직접 CoroutineDispatcher 객체를 생성하는 문제를 방지하기 위해 미리 정의된 CoroutineDispatcher의 목록을 제공 (아래)
     *  > Dispatchers.IO, Dispatchers.Default, Dispatchers.Main
     *  > Dispatchers.IO : 프로세서의 수와 64 중 큰 값으로 설정 (최소 64개 보장)
     *  > Dispatchers.Default : 프로세서 수만큼 스레드 생성
     * Dispatchers.IO, Dispatchers.Default는 서로 같은 스레드 풀을 사용한다. 코루틴 라이브러리의 공유 스레드풀을 사용한다.
     * launch 함수에서 CoroutineDispatcher 를 명시하지 않으면 Dispatchers.Default 가 사용된다.
     */
    @Test
    fun test_dispatcher_child_coroutine() = runBlocking<Unit> {
        val dispatcher = newFixedThreadPoolContext(2, "MultiThread")

        launch(dispatcher) {
            logCoroutineName("부모 코루틴 실행")

            // 부모 코루틴의 CoroutineDispatcher 를 사용
            launch {
                logCoroutineName("자식1 코루틴 실행")
            }

            // 부모 코루틴의 CoroutineDispatcher 를 사용
            launch {
                logCoroutineName("자식2 코루틴 실행")
            }
        }
    }

    @Test
    fun test_dispatcher_io() = runBlocking<Unit> {
        launch(Dispatchers.IO) {
            logCoroutineName()
        }
    }

    /**
     * 10번 반복해서 launch 빌더를 사용하여 코루틴을 생성한다.
     * 여러개의 DefaultDispatcher-worker- 스레드가 사용되어짐을 알 수 있다. (자체 스레드 풀을 사용)
     */
    @Test
    fun test_dispatcher_io2() = runBlocking {
        repeat(100) {
            launch(Dispatchers.IO) {
                logCoroutineName()
            }
        }
    }

    /**
     * Dispatchers.Default는 CPU 바운드 작업이 필요할 때 사용, 오래 걸리는 작업, 배치 처리 등
     * CPU 바운드 작업 : CPU 계산 능력에 의존하는 작업을 의미한다.
     */
    @Test
    fun test_dispatcher_default() = runBlocking<Unit> {
        launch(Dispatchers.Default) {
            logCoroutineName()
        }
    }

    @Test
    fun test_dispatcher_default2() = runBlocking {
        repeat(100) {
            launch(Dispatchers.Default) {
                logCoroutineName()
            }
        }
    }

    @Test
    fun test_dispatcher_limit() = runBlocking<Unit> {
        launch(Dispatchers.Default.limitedParallelism(2)) { // 여러개의 스레드 중 2개의 스레드만 사용됨
            repeat(10) {// 10개의 코루틴 실행
                launch {
                    println("[${Thread.currentThread().name}] 실행")
                }
            }
        }
    }

    /**
     * 예외 발생, Main 스레드는 안드로이드에서만 사용 가능
     * java.lang.IllegalStateException: Module with the Main dispatcher is missing. Add dependency providing the Main dispatcher, e.g. 'kotlinx-coroutines-android' and ensure it has the same version as 'kotlinx-coroutines-core'
     */
    @Test
    fun test_dispatcher_main() = runBlocking<Unit> {
        launch(Dispatchers.Main) {
            logCoroutineName()
        }
    }

    private fun logCoroutineName(prefix: String) {
        println("$prefix - ${Thread.currentThread().name}")
    }

    private fun logCoroutineName() {
        println(Thread.currentThread().name)
    }
}



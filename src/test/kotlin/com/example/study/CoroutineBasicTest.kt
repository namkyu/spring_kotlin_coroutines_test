package com.example.study

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class CoroutineBasicTest {

    @Test
    fun test() {
        // 메인 쓰레드에서 실행
        logCoroutineName("Start")

        // runBlocking 함수가 호출되면 하나의 코루틴이 생성
        runBlocking {
            // 메인 쓰레드 > 코루틴1 에서 실행
            logCoroutineName("Start Coroutine")
        }

        logCoroutineName("End")
    }

    @Test
    fun test1() = runBlocking {

        logCoroutineName("Hello World! Start")

        // 코루틴 추가 생성
        launch {
            logCoroutineName("launch Start")
        }

        logCoroutineName("Hello World! End")

        // 제어권 코루틴 1번이 가지고 있음. 그래서 Start End 까지 다 처리 후 launch로 제어권 넘겨줌
    }

    @Test
    fun test2() = runBlocking {

        launch {
            // 메인 쓰레드 > 코루틴2 에서 실행
            logCoroutineName("launch A Start")

            // 메인 쓰레드 > 코루틴2 에서 실행
            logCoroutineName("launch A End")
        }

        launch {
            // 메인 쓰레드 > 코루틴3 에서 실행
            logCoroutineName("launch B Start")

            // 메인 쓰레드 > 코루틴3 에서 실행
            logCoroutineName("launch B End")
        }

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World!")

        // 총 3개의 코루틴 생성
    }

    // 코루틴 이름 부여
    @Test
    fun test2_1() = runBlocking(context = CoroutineName("mc")) {

        launch(CoroutineName("Coroutine1")) {
            logCoroutineName("launch A Start")
            logCoroutineName("launch A End")
        }

        launch(CoroutineName("Coroutine2")) {
            logCoroutineName("launch B Start")
            logCoroutineName("launch B End")
        }

        logCoroutineName("Hello World!")
    }

    @Test
    fun test3() = runBlocking {

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! Start")

        launch {
            // 메인 쓰레드 > 코루틴2 에서 실행
            logCoroutineName("launch A Start")

            // 1초 비동기 멈춤
            delay(1000L)

            // 메인 쓰레드 > 코루틴2 에서 실행
            logCoroutineName("launch A End")
        }

        launch {
            // 메인 쓰레드 > 코루틴3 에서 실행
            logCoroutineName("launch B Start")

            // 메인 쓰레드 > 코루틴3 에서 실행
            logCoroutineName("launch B End")
        }

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! End")

//        Hello World! Start - Test worker @coroutine#1 thread!
//        Hello World! End - Test worker @coroutine#1 thread!
//        launch A Start - Test worker @coroutine#2 thread!
//        launch B Start - Test worker @coroutine#3 thread!
//        launch B End - Test worker @coroutine#3 thread!
//        launch A End - Test worker @coroutine#2 thread!

        // test2 변과 연결되어 있음
        // delay()를 통해 코루틴을 일시정지 하면 해당 코루틴은 잠시 멈추지만 다른 코루틴은 해당 스레드에서 동작할 수 있다고 했다.
        // 이 말은 다음과 같은 그림으로 표현할 수 있다.
        // 이것도 제어권을 누가 가지고 있는지 잘 파악하고 생각하면 된다.
    }

    @Test
    fun test4() = runBlocking {

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! Start")

        launch {
            // 메인 쓰레드 > 코루틴2 에서 실행
            logCoroutineName("launch A Start")

            // 1초 비동기 멈춤
            delay(1000L)

            // 메인 쓰레드 > 코루틴2 에서 실행
            logCoroutineName("launch A End")
        }

        launch {
            // 메인 쓰레드 > 코루틴3 에서 실행
            logCoroutineName("launch B Start")

            // 1초 비동기 멈춤
            delay(1000L)

            // 메인 쓰레드 > 코루틴3 에서 실행
            logCoroutineName("launch B End")
        }

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! End")

//        Hello World! Start - Test worker @coroutine#1 thread!
//        Hello World! End - Test worker @coroutine#1 thread!
//        launch A Start - Test worker @coroutine#2 thread!
//        launch B Start - Test worker @coroutine#3 thread!
//        launch A End - Test worker @coroutine#2 thread!
//        launch B End - Test worker @coroutine#3 thread!
        // 제어권 을 누가 가지고 있는지 생각하기
    }

    @Test
    fun test44() = runBlocking {

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! Start")

        launch {
            launchAA()
        }
        launch {
            launchBB()
        }

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! End")

//        Hello World! Start - Test worker @coroutine#1
//        Hello World! End - Test worker @coroutine#1
//        launch AA Start - Test worker @coroutine#2
//        launch BB Start - Test worker @coroutine#3
//        launch AA End - Test worker @coroutine#2
//        launch BB End - Test worker @coroutine#3
    }

    private suspend fun launchAA() {
        // suspend 함수 내부가 coroutineScope인 것은 아니다.
        logCoroutineName("launch AA Start")
        delay(1000L)
        logCoroutineName("launch AA End")
    }

    private suspend fun launchBB() {
        logCoroutineName("launch BB Start")
        delay(1000L)
        logCoroutineName("launch BB End")
    }

    @Test
    fun test5() = runBlocking {

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! Start")

        // launch A
        launchA()
        // launch B
        launchB()

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! End")

        // 이런 경우에는 launchA()에서 실행한 코루틴을 일시정지 한다고 해도 해당 coroutineScope 내에 존재하는 다른 코루틴이 없기 때문에 제어권을 넘겨줄 곳이 없다.
        // 따라서 일시정지한 시간만큼 정지한 후 다시 해당 코루틴이 재개된다. 따라서 처음에 기대한 결과와는 다르게 동작한다.
        // launch A Start, launch A End가 출력된 후에 launchB()가 실행될 것이다.
        // 그 후에 마지막으로 runBlocking 내부의 작업이 실행된다.
    }

    private suspend fun launchA() = coroutineScope {
        launch {
            logCoroutineName("launch A Start")
            delay(1000L)
            logCoroutineName("launch A End")
        }
    }

    private suspend fun launchB() = coroutineScope {
        launch {
            logCoroutineName("launch B Start")
            delay(1000L)
            logCoroutineName("launch B End")
        }
    }

    @Test
    fun test6() = runBlocking {
        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! Start")

        withContext(coroutineContext) {

            launch {
                // 메인 쓰레드 > 코루틴2 에서 실행
                logCoroutineName("launch A Start")

                // 1초 비동기 멈춤
                delay(1000L)

                // 메인 쓰레드 > 코루틴2 에서 실행
                logCoroutineName("launch A End")
            }

            launch {
                // 메인 쓰레드 > 코루틴3 에서 실행
                logCoroutineName("launch B Start")

                // 메인 쓰레드 > 코루틴3 에서 실행
                logCoroutineName("launch B End")
            }
        }

        // 메인 쓰레드 > 코루틴1 에서 실행
        logCoroutineName("Hello World! End")

//        Hello World! Start - Test worker @coroutine#1 thread!
//        launch A Start - Test worker @coroutine#2 thread!
//        launch B Start - Test worker @coroutine#3 thread!
//        launch B End - Test worker @coroutine#3 thread!
//        launch A End - Test worker @coroutine#2 thread!
//        Hello World! End - Test worker @coroutine#1 thread!
    }

    private fun logCoroutineName(prefix: String) {
        println("$prefix - ${Thread.currentThread().name}")
    }

    private fun logCoroutineName() {
        println(Thread.currentThread().name)
    }

}



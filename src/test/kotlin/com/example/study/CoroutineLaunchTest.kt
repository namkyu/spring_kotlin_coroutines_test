package com.example.study

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class CoroutineLaunchTest {

    /**
     * job 객체는 코루틴의 상태를 추적하고 제어하는 데 사용한다.
     */
    @Test
    fun test_coroutine_job() = runBlocking {
        val job = launch {
            logCoroutineName()
        }
    }

    /**
     * updateTokenJob 코루틴이 완료되고 난 뒤에 networkCallJob 코루틴이 실행되어야 한다.
     * 토큰 업데이트 시작/완료가 다 이뤄진 후에 네트워크 요청이 이뤄져야 하지만 그렇게 동작하지 않는다. (오류)
     */
    @Test
    fun test_coroutine_job2() = runBlocking {
        val updateTokenJob = launch(Dispatchers.IO) {
            logCoroutineName("토큰 업데이트 시작")
            delay(100L)
            logCoroutineName("토큰 업데이트 완료")
        }

        val networkCallJob = launch(Dispatchers.IO) {
            logCoroutineName("네트워크 요청")
        }
    }

    /**
     * updateTokenJob 코루틴이 완료될 때까지 runBlocking 코루틴 일시 중단
     * join 함수는 join 함수를 호출한 코루틴을 제외하고 이미 실행 중인 다른 코루틴을 일시 중단하지 않는다. (다른 스레드에서 이미 실행이 시작된 코루틴은 일시 중단에 영향을 받지 않는다.)
     */
    @Test
    fun test_coroutine_job_join() = runBlocking {
        println("[${Thread.currentThread().name}]")

        val updateTokenJob = launch(Dispatchers.IO) {
            logCoroutineName("토큰 업데이트 시작")
            delay(100L)
            logCoroutineName("토큰 업데이트 완료")
        }

        // Test worker @coroutine#1 이 join을 만나면 중단된다. 이런 원리로 인해서 networkCallJob 코드가 실행 안 됨
        updateTokenJob.join()

        val networkCallJob = launch(Dispatchers.IO) {
            logCoroutineName("네트워크 요청")
        }
    }

    /**
     * 이미지를 변환하는 작업은 CPU 바운드 작업이므로 각 코루틴을 Dispatchers.Default 에 실행 요청한다.
     * 내부에서는 Thread.sleep 함수를 사용해 작업이 진행되는 동안 스레드를 블로킹한다.
     */
    @Test
    fun test_coroutine_job_joinAll() = runBlocking {
        val convertImageJob1: Job = launch(Dispatchers.Default) {
            delay(1000L)
            logCoroutineName("이미지1 변환 완료")
        }

        val convertImageJob2: Job = launch(Dispatchers.Default) {
            delay(1000L)
            logCoroutineName("이미지2 변환 완료")
        }

        // 이미지 1, 2 변환될 때까지 대기
        joinAll(convertImageJob1, convertImageJob2)

        val uploadImageJob = launch(Dispatchers.IO) {
            logCoroutineName("이미지 1, 2 업로드")
        }
    }

    // 종종 코루틴을 먼저 생성해 놓고 나중에 실행해야 하는 경우가 있을 수 있다.
    @Test
    fun test_coroutine_launch_lazy() = runBlocking {
        val executionTime = measureTimeMillis {
            val job = launch(start = CoroutineStart.LAZY) {
                logCoroutineName()
            }

            delay(1000L)
            job.start()
        }

        println("${executionTime}ms 소요됨")
    }

    @Test
    fun test_coroutine_launch_cancle() = runBlocking {
        val executionTime = measureTimeMillis {
            val longJob = launch(Dispatchers.Default) {
                repeat(10) {
                    delay(1000L)
                    println("[${Thread.currentThread().name}] $it 실행")
                }
            }

            delay(3000L)
            longJob.cancel() // 코루틴 취소 (코루틴은 일시 중단되는 시점에 코루틴의 취소를 확인하기 때문에 작업 중간에 delay(1L) 을 주게 되면 일시 중단 후 취소를 확인 한다.)
            longJob.cancelAndJoin() // longJob 코루틴이 취소 완료될 때까지 runBlocking 코루틴이 일시 중단된다. (취소 완료 후 다음 코드 실행이 필요한 로직에서 사용)

            println("isActive : ${longJob.isActive}") // 코루틴 활성화 여부
            println("isCancelled : ${longJob.isCancelled}") // 코루틴 취소 여부
            println("isCompleted : ${longJob.isCompleted}") // 코루틴 완료 여부
        }

        println("${executionTime}ms 소요됨")
    }

    // runBlocking 코루틴(부모) > launch 코루틴(자식) > launch 코루틴(자식)
    // 부모 코루틴 실행 환경이 자식 코루틴에게 상속된다.
    // 부모 코루틴이 취소되면 자식 코루틴도 취소된다. (밑으로만 취소 전파)
    // 부모 코루틴은 자식 코루틴이 완료될 때까지 대기한다.
    // CroutineScope를 사용해 코루틴이 실행되는 범위를 제한할 수 있다.
    @Test
    fun test_coroutine_launch_parent_child(): Unit = runBlocking {
        logCoroutineName()

        val coroutineContext = newSingleThreadContext("MyThread") + CoroutineName("CoroutineA")
        launch(coroutineContext) {
            logCoroutineName("부모 코루틴 생성")
            launch {
                logCoroutineName("자식 코루틴 생성")
            }
        }
    }

    // db1, 2, 3으로부터 데이터를 가져오는 작업을 하는 자식 코루틴 3개 생성
    // parentJob 코루틴을 취소하기 때문에 자식 코루틴 모두 취소된다.
    @Test
    fun test_coroutine_cancel_parent_job() = runBlocking {
        val parentJob = launch(Dispatchers.IO) {
            val dbResultsDeferred: List<Deferred<String>> = listOf("db1", "db2", "db3").map {
                async {
                    logCoroutineName("$it 에서 데이터 가져오기")
                    delay(1000L)
                    return@async it
                }
            }

            val dbResults: List<String> = dbResultsDeferred.awaitAll() // 모든 코루틴이 완료될 때까지 대기
            println(dbResults)
        }

//        delay(2000L)
        parentJob.cancel() // 부모 코루틴 취소 요청
    }

    @Test
    fun test_coroutine_launch_parent_child_job(): Unit = runBlocking {
        logCoroutineName()
        val runBlockingJob = coroutineContext[Job]

        launch {
            logCoroutineName()
            val launchJob = coroutineContext[Job]

            if (runBlockingJob == launchJob) {
                println("job 동일")
            } else {
                println("job 다름")
            }
        }
    }

    private fun logCoroutineName(prefix: String) {
        println("$prefix - ${Thread.currentThread().name}")
    }

    private fun logCoroutineName() {
        println(Thread.currentThread().name)
    }
}



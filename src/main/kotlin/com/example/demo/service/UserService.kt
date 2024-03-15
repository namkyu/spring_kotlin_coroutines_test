package com.example.demo.service

import com.example.demo.controller.UserPurchaseRequest
import com.example.demo.entity.User
import com.example.demo.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import kotlin.system.measureTimeMillis
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Service
class UserService(private val userRepository: UserRepository) {

    suspend fun registerUser(userName: String, email: String): Unit = coroutineScope {

        val executionTime = measureTimeMillis {
            // 사용자 등록
            userRepository.save(User(userName = userName, email = email))

            // 이메일 전송
            sendEmail()
            // 카카오 메세지 발송
            sendKakao()
        }

        println("${executionTime}ms 소요됨")
    }

    suspend fun registerUser2(userName: String, email: String): Unit = coroutineScope {

        val executionTime = measureTimeMillis {
            // 사용자 등록
            launch {
                logCoroutineName("saveUser")
                userRepository.save(User(userName = userName, email = email))
            }

            // 이메일 전송
            launch {
                sendEmail()
            }

            // 카카오 메세지 발송
            launch {
                sendKakao()
            }
        }

        println("${executionTime}ms 소요됨")
    }

    // 상품 정보 -> 구매 처리 -> 구매 이메일 발송 -> 배송 처리
    suspend fun purchase(request: UserPurchaseRequest) = coroutineScope {
        val userId = request.userId
        val goodsId = request.goodsId
        val quantity = request.quantity

        val executionTime = measureTimeMillis {
            // 상품 정보
            val goodsInfo = getGoodsInfo(goodsId)

            // 구매 진행
            launch {
                purchase(userId, goodsId, goodsInfo.price * quantity)
            }

            // 구매 완료 이메일 전송
            launch {
                sendEmail()
            }

            // 배송 처리
            launch {
                val userInfo = getUserInfo(userId)
                delivery(goodsInfo.name, userInfo.address)
            }
        }

        println("${executionTime}ms 소요됨")
    }

    suspend fun purchase1(request: UserPurchaseRequest) = coroutineScope {
        val userId = request.userId
        val goodsId = request.goodsId
        val quantity = request.quantity
        val executionTime = measureTimeMillis {
            // 상품 정보
            val goodsInfo = getGoodsInfo(goodsId)

            // 구매 진행
            launch {
                purchase(userId, goodsId, goodsInfo.price * quantity)
            }

            // 구매 완료 이메일 전송
            sendEmail()

            // 배송 처리
            val userInfo = getUserInfo(userId)
            delivery(goodsInfo.name, userInfo.address)
        }

        println("${executionTime}ms 소요됨")
    }

    suspend fun purchaseHistory(userId: String) = coroutineScope {
        // 상품 결과
        val resultListDeferred: MutableList<Deferred<History>> = mutableListOf()

        val executionTime = measureTimeMillis {
            // 구매 이력
            val purchaseHistories = getPurchaseHistory(userId)

            for (purchaseId in purchaseHistories) {
                // 상품 정보 가져오기
                val deferred = async {
                    val goodsInfo = getGoodsInfo(purchaseId)
                    History(userId, goodsInfo.goodsId, goodsInfo.price)
                }
                resultListDeferred.add(deferred)
            }

            resultListDeferred.awaitAll()
        }

        logCoroutineName("purchase end")
        println("${executionTime}ms 소요됨")
        println(resultListDeferred.map { it.getCompleted() })
    }

    suspend fun occursException(): Unit = coroutineScope {
        val executionTime = measureTimeMillis {
            // 이메일 전송
            sendEmail()

            try {
                // 카카오 메세지 발송
                sendKakaoException()
            } catch (e: Exception) {
                println("try catch 예외 처리")
            }
        }

        println("${executionTime}ms 소요됨")
    }

    suspend fun occursException2(): Unit = coroutineScope {
        val executionTime = measureTimeMillis {
            try {
                // 카카오 메세지 발송
                sendKakaoExceptionWithScope()
            } catch (e: Exception) {
                println(e.message)
                e.printStackTrace()
            }
        }

        println("${executionTime}ms 소요됨")
    }

    private suspend fun sendEmail() {
        logCoroutineName("sendEmail start")
        delay(1000L) // simulate sending email
        logCoroutineName("sendEmail end")
        println("이메일 전송 완료")
    }

    private suspend fun sendKakao() {
        logCoroutineName("sendKakao start")
        delay(1000L) // simulate sending teams
        logCoroutineName("sendKakao end")
        println("카카오 메세지 전송 완료")
    }

    private suspend fun sendKakaoException() {
        logCoroutineName("sendKakao")
        delay(1000L) // simulate sending teams
        throw IllegalArgumentException("카카오 메세지 전송 실패")
    }

    private suspend fun sendKakaoExceptionWithScope() {
        coroutineScope {
            launch {
                logCoroutineName("sendKakao")
                delay(1000L) // simulate sending teams
                throw IllegalArgumentException("카카오 메세지 전송 실패")
            }
        }
    }

    private suspend fun getPurchaseHistory(userId: String): List<Long> {
        logCoroutineName("getPurchaseHistory start")
        delay(1000L)
        logCoroutineName("getPurchaseHistory end")
        return listOf(1000, 1001, 1002, 1003)
    }

    private suspend fun delivery(goodsName: String, address: String) {
        logCoroutineName("delivery start")
        delay(1000L)
        logCoroutineName("delivery end")
    }

    private suspend fun getUserInfo(userId: String): UserInfo {
        logCoroutineName("getUserInfo start")
        delay(1000L)
        logCoroutineName("getUserInfo end")
        return UserInfo("램규", "지구", userId)
    }

    private suspend fun getGoodsInfo(goodsId: Long): GoodsInfo {
        logCoroutineName("getGoodsInfo start")
        delay(1000L)
        logCoroutineName("getGoodsInfo end")
        return GoodsInfo("아이폰13", 10000, goodsId)
    }

    private suspend fun purchase(userId: String, goodsId: Long, amount: Long) {
        println("구매 처리 - userId: $userId, goodsId: $goodsId, amount: $amount")
        logCoroutineName("purchase start")
        delay(1000L)
        logCoroutineName("purchase end")
    }

    suspend fun logCoroutineName(prefix: String) {
        val time = getTime()
        println("$prefix - [${Thread.currentThread().name}] $time")
    }

    suspend fun getAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }

    fun getTime(): String {
        val currentEpochMilli = Instant.now().toEpochMilli()
        val currentDateTime = LocalDateTime.ofEpochSecond(currentEpochMilli / 1000, (currentEpochMilli % 1000).toInt() * 1000000, ZoneOffset.UTC)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        return currentDateTime.format(formatter)
    }
}

data class UserInfo(val name: String, val address: String, val userId: String)

data class GoodsInfo(val name: String, val price: Long, val goodsId: Long)

data class History(val userId: String, val goodsId: Long, val amount: Long)
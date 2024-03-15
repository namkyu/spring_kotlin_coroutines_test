package com.example.co.common.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfig {

    @Bean
    fun defaultWebClient(httpClient: HttpClient): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:8080") // 기본 URL (중복 제거, 코드 간결)
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }

    @Bean
    fun httpClient(): HttpClient {
        val connectionProvider = ConnectionProvider.builder("custom")
            .maxConnections(100) // 최대 연결 수 설정
            .pendingAcquireMaxCount(10) // 대기 중인 연결 요청의 최대 수 설정
            .pendingAcquireTimeout(Duration.ofSeconds(5)) // 대기 중인 연결 요청 타임아웃 설정
            .build()

        return HttpClient.create(connectionProvider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .doOnConnected { connection: Connection -> // { } 람다 표현식
                connection.addHandlerLast(ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)) // 클라이언트가 서버로부터 데이터를 읽는 동안 일정 시간 내에 데이터를 받지 못하면 타임아웃이 발생
                connection.addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)) // 클라이언트가 서버로 데이터를 보내는 동안 일정 시간 내에 데이터를 보내지 못하면 타임아웃이 발생
            }
    }
}
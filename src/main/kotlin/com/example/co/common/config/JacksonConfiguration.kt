package com.example.co.common.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfiguration {

    // 직렬화 : java -> json 형식의 문자열로 변환
    // 역직렬화 : json -> java 형식의 객체로 변환
    @Bean
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        )
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL) // 직렬화 : null 값은 제외
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false) // 직렬화 : 날짜와 시간을 json 으로 직렬화할 때 ISO-8601 형식으로 변환하는 옵션 설정
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false) // 직렬화 : 빈 객체(프로퍼티가 없는)를 json 으로 변환할 때 실패하지 않도록 설정
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 역직렬화 : 알려지지 않는 프로퍼티가 포함되어 있을 때 예외 발생 방지
        return objectMapper
    }
}
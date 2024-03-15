package com.example.co.api

import org.hibernate.exception.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime


@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun constraintViolationException(ex: ConstraintViolationException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST.message)
        problemDetail.setProperty("timestamp", LocalDateTime.now())
        return problemDetail
    }

    @ExceptionHandler(value = [CustomException::class])
    fun handlingCustomException(ex: CustomException): ProblemDetail {
        val errorCode: ErrorCode = ex.errorCode
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, errorCode.message)
    }
}

class CustomException(
    val errorCode: ErrorCode
) : RuntimeException()

enum class ErrorCode(val status: HttpStatus, val message: String) {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 입니다."),
}
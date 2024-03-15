package com.example.co.common.constants

enum class TestCode(val code: String) {
    AAA("aaa"),
    BBB("bbb")
    ;

    // static 메서드와 비슷함
    companion object {
        fun find(code: String) = values().find {
            it.code == code
        } ?: throw IllegalArgumentException("not found code: $code")

        fun exists(gameCode: String) = values().any {
            it.code == gameCode
        }
    }
}
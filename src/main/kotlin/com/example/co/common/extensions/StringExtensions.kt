package com.example.co.common.extensions

import java.text.NumberFormat

// 코틀린 확장 함수는 정적인 형태로 코드가 생성된다.
// 범위(scope)를 주의하고, 멤버 등과의 혼용이 일어나지 않도록 신경써야 함

fun String.addExclamation(): String {
    return "$this!"
}

fun Int.convertToMileage(): String {
    return NumberFormat.getNumberInstance().format(this) + "km"
}

fun List<Int>.getHigherThan(num: Int): List<Int> {
    val result = arrayListOf<Int>()
    for (item in this) { // this는 list를 의미
        if (item > num) {
            result.add(item)
        }
    }

    return result
}
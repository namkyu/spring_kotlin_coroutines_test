package com.example.co.common.utils

import org.modelmapper.ModelMapper

class ModelMapperUtils {

    companion object {
        fun <T> map(src: Any, dest: Class<T>): T {
            return ModelMapper().map(src, dest)
        }

        // inline 함수는 컴파일 단계에서 호출 방식이 아닌 코드 자체가 복사되는 방식으로 변환
        inline fun <reified S, reified D> mapToList(srcList: List<S>): List<D> {
            val modelMapper = ModelMapper()
            return srcList.map { modelMapper.map(it, D::class.java) } // it은 list의 각 요소를 나타냄
        }
    }
}
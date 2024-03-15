package com.example.co.api.valid

import jakarta.validation.Constraint
import kotlin.reflect.KClass


@Constraint(validatedBy = [TestCodeValidator::class])
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidTestCode(
    val message: String = "invalid testCode",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = [],
)
package com.example.co.api.valid

import com.example.co.common.constants.TestCode
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext


class TestCodeValidator : ConstraintValidator<ValidTestCode, String> {

    private var defaultMessage: String? = null

    override fun initialize(constraintAnnotation: ValidTestCode) {
        defaultMessage = constraintAnnotation.message
    }

    override fun isValid(gameCode: String, context: ConstraintValidatorContext): Boolean {
        return TestCode.exists(gameCode)
    }
}
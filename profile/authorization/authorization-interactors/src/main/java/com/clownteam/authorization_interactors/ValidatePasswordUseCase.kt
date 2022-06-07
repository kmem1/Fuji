package com.clownteam.authorization_interactors

import com.clownteam.authorization_interactors.IValidatePasswordUseCase.Companion.MIN_PASSWORD_LENGTH
import com.clownteam.core.domain.IUseCase

internal class ValidatePasswordUseCase : IValidatePasswordUseCase {


    override suspend fun invoke(param: String): ValidatePasswordResult {

        if (param.length < MIN_PASSWORD_LENGTH) {
            return ValidatePasswordResult.NotEnoughLengthError
        }

        val containsLettersAndDigits = param.any { it.isDigit() } && param.any { it.isLetter() }

        if (!containsLettersAndDigits) {
            return ValidatePasswordResult.ShouldContainLettersAndDigitsError
        }

        val containsLowerCase = param.any { it.isLowerCase() }
        val containsUpperCase = param.any { it.isUpperCase() }

        if (!(containsLowerCase && containsUpperCase)) {
            return ValidatePasswordResult.ShouldContainLowerAndUpperCaseError
        }

        val containsNonLetter = param.any { !it.isLetterOrDigit() }

        if (!containsNonLetter) {
            return ValidatePasswordResult.ShouldContainNonLetterSymbol
        }

        return ValidatePasswordResult.Success
    }
}

interface IValidatePasswordUseCase : IUseCase.InOut<String, ValidatePasswordResult> {
    companion object {
        const val MIN_PASSWORD_LENGTH = 8
    }
}

sealed class ValidatePasswordResult {
    object NotEnoughLengthError : ValidatePasswordResult()
    object ShouldContainLettersAndDigitsError : ValidatePasswordResult()
    object ShouldContainLowerAndUpperCaseError: ValidatePasswordResult()
    object ShouldContainNonLetterSymbol: ValidatePasswordResult()
    object Success : ValidatePasswordResult()
}
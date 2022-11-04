package com.clownteam.core.interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.interactors.IValidateLoginUseCase.Companion.MIN_LOGIN_LENGTH

internal class ValidateLoginUseCase : IValidateLoginUseCase {

    override suspend fun invoke(param: String): ValidateLoginResult {

        if (param.isBlank()) {
            return ValidateLoginResult.BlankLoginError
        }

        if (param.length < MIN_LOGIN_LENGTH) {
            return ValidateLoginResult.ShortLoginError
        }

        return ValidateLoginResult.Success
    }
}

interface IValidateLoginUseCase : IUseCase.InOut<String, ValidateLoginResult> {
    companion object {
        const val MIN_LOGIN_LENGTH = 3

        fun create(): IValidateLoginUseCase = ValidateLoginUseCase()
    }
}

sealed class ValidateLoginResult {
    object BlankLoginError: ValidateLoginResult()
    object ShortLoginError: ValidateLoginResult()
    object Success: ValidateLoginResult()
}
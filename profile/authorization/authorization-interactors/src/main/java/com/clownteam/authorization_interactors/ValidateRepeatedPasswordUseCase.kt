package com.clownteam.authorization_interactors

import com.clownteam.core.domain.IUseCase

internal class ValidateRepeatedPasswordUseCase : IValidateRepeatedPasswordUseCase {

    override suspend fun invoke(param: ValidateRepeatedPasswordParams): ValidateRepeatedPasswordResult {
        if (param.repeatedPassword.isBlank()) {
            return ValidateRepeatedPasswordResult.BlankError
        }

        if (param.password != param.repeatedPassword) {
            return ValidateRepeatedPasswordResult.NotMatchesError
        }

        return ValidateRepeatedPasswordResult.Success
    }
}

interface IValidateRepeatedPasswordUseCase : IUseCase.InOut<ValidateRepeatedPasswordParams, ValidateRepeatedPasswordResult>

data class ValidateRepeatedPasswordParams(
    val password: String,
    val repeatedPassword: String
)

sealed class ValidateRepeatedPasswordResult {
    object BlankError: ValidateRepeatedPasswordResult()
    object NotMatchesError: ValidateRepeatedPasswordResult()
    object Success: ValidateRepeatedPasswordResult()
}
package com.clownteam.authorization_interactors

import com.clownteam.core.domain.IUseCase

internal class ValidateEmailUseCase : IValidateEmailUseCase {

    private val emailRegex =
            Regex("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+")


    override suspend fun invoke(param: String): ValidateEmailResult {

        if (param.isBlank()) {
            return ValidateEmailResult.BlankEmailError
        }

        if (!emailRegex.matches(param)) {
            return ValidateEmailResult.InvalidEmailError
        }

        return ValidateEmailResult.Success
    }
}

interface IValidateEmailUseCase : IUseCase.InOut<String, ValidateEmailResult>

sealed class ValidateEmailResult {
    object BlankEmailError: ValidateEmailResult()
    object InvalidEmailError: ValidateEmailResult()
    object Success: ValidateEmailResult()
}
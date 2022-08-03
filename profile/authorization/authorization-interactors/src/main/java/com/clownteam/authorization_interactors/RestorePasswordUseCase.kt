package com.clownteam.authorization_interactors

import com.clownteam.authorization_datasource.network.AuthorizationService
import com.clownteam.core.domain.IUseCase

internal class RestorePasswordUseCase(
    private val authorizationService: AuthorizationService
) : IRestorePasswordUseCase {

    override suspend fun invoke(param: String): RestorePasswordUseCaseResult {
        val result = authorizationService.restorePassword(param)

        if (result.isNetworkError) return RestorePasswordUseCaseResult.NetworkError

        return if (result.isSuccessCode) {
            RestorePasswordUseCaseResult.Success
        } else {
            RestorePasswordUseCaseResult.Failed
        }
    }
}

interface IRestorePasswordUseCase : IUseCase.InOut<String, RestorePasswordUseCaseResult>

sealed class RestorePasswordUseCaseResult {

    object Success : RestorePasswordUseCaseResult()

    object Failed : RestorePasswordUseCaseResult()

    object NetworkError : RestorePasswordUseCaseResult()
}
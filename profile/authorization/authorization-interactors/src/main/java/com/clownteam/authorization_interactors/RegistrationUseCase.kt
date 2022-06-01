package com.clownteam.authorization_interactors

import com.clownteam.authorization_datasource.network.AuthorizationService
import com.clownteam.authorization_domain.registration.RegistrationData
import com.clownteam.core.domain.IUseCase

internal class RegistrationUseCase(
    private val authorizationService: AuthorizationService
) : IRegistrationUseCase {

    override suspend fun invoke(param: RegistrationData): RegistrationUseCaseResult {
        val result = authorizationService.register(param)

        if (result.isNetworkError) return RegistrationUseCaseResult.NetworkError

        return if (result.isSuccessCode) {
            RegistrationUseCaseResult.Success
        } else {
            RegistrationUseCaseResult.Failed(errorMessage = result.errorMessage)
        }
    }
}

interface IRegistrationUseCase : IUseCase.InOut<RegistrationData, RegistrationUseCaseResult>

sealed class RegistrationUseCaseResult {

    object Success : RegistrationUseCaseResult()

    class Failed(val errorMessage: String = "") : RegistrationUseCaseResult()

    object NetworkError : RegistrationUseCaseResult()
}
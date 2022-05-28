package com.clownteam.authorization_interactors

import com.clownteam.authorization_datasource.network.AuthorizationService
import com.clownteam.authorization_domain.registration.RegistrationData
import com.clownteam.core.domain.IUseCase

internal class RegisterUseCase(
    private val authorizationService: AuthorizationService
) : IRegisterUseCase {

    override suspend fun invoke(param: RegistrationData): RegistrationUseCaseResult {
        val result = authorizationService.register(param)

        return if (result) {
            RegistrationUseCaseResult.Success
        } else {
            RegistrationUseCaseResult.Failed
        }
    }
}

interface IRegisterUseCase : IUseCase.InOut<RegistrationData, RegistrationUseCaseResult>

sealed class RegistrationUseCaseResult {

    object Success : RegistrationUseCaseResult()

    object Failed : RegistrationUseCaseResult()
}
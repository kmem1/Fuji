package com.clownteam.authorization_interactors

import com.clownteam.authorization_datasource.network.AuthorizationService
import com.clownteam.authorization_domain.login.LoginData
import com.clownteam.core.domain.IUseCase

internal class LoginUseCase(
    private val authorizationService: AuthorizationService
) : ILoginUseCase {

    override suspend fun invoke(param: LoginData): LoginUseCaseResult {
        val result = authorizationService.login(param)

        if (result.isNetworkError) return LoginUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data?.access != null && result.data?.refresh != null) {
            val accessToken = result.data?.access ?: ""
            val refreshToken = result.data?.refresh ?: ""
            LoginUseCaseResult.Success(accessToken, refreshToken)
        } else {
            LoginUseCaseResult.Failed(errorMessage = result.errorMessage)
        }
    }
}

interface ILoginUseCase : IUseCase.InOut<LoginData, LoginUseCaseResult>

sealed class LoginUseCaseResult {

    class Success(val accessToken: String, val refreshToken: String) : LoginUseCaseResult()

    class Failed(val errorMessage: String = "") : LoginUseCaseResult()

    object NetworkError : LoginUseCaseResult()
}
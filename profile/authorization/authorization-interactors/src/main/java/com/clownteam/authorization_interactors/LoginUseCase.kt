package com.clownteam.authorization_interactors

import com.clownteam.authorization_datasource.network.AuthorizationService
import com.clownteam.authorization_domain.login.LoginData
import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

internal class LoginUseCase(
    private val authorizationService: AuthorizationService,
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager
) : ILoginUseCase {

    override suspend fun invoke(param: LoginData): LoginUseCaseResult {
        val result = authorizationService.login(param)

        if (result.isNetworkError) return LoginUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data?.access != null && result.data?.refresh != null) {
            val accessToken = result.data?.access ?: ""
            val refreshToken = result.data?.refresh ?: ""

            tokenManager.setToken(accessToken)
            tokenManager.setRefresh(refreshToken)

            setUserPath()

            LoginUseCaseResult.Success
        } else {
            LoginUseCaseResult.Failed(errorMessage = result.errorMessage)
        }
    }

    private suspend fun setUserPath() {
        val result = authorizationService.getUserPath(token = tokenManager.getToken() ?: "")

        if (result.isSuccessCode && result.data?.path != null) {
            userDataManager.setUserPath(result.data?.path ?: "")
        }
    }
}

interface ILoginUseCase : IUseCase.InOut<LoginData, LoginUseCaseResult>

sealed class LoginUseCaseResult {

    object Success : LoginUseCaseResult()

    class Failed(val errorMessage: String = "") : LoginUseCaseResult()

    object NetworkError : LoginUseCaseResult()
}
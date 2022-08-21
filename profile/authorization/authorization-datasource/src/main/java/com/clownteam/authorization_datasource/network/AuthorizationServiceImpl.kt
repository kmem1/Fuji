package com.clownteam.authorization_datasource.network

import com.clownteam.authorization_datasource.network.login.LoginRequestBody
import com.clownteam.authorization_datasource.network.login.LoginResponse
import com.clownteam.authorization_datasource.network.register.RegisterRequestBody
import com.clownteam.authorization_datasource.network.register.RegisterResponse
import com.clownteam.authorization_datasource.network.user_path.UserPathResponse
import com.clownteam.authorization_domain.login.LoginData
import com.clownteam.authorization_domain.registration.RegistrationData
import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest

@Suppress("BlockingMethodInNonBlockingContext")
class AuthorizationServiceImpl(private val api: AuthorizationApi) : AuthorizationService {

    override suspend fun register(data: RegistrationData): NetworkResponse<RegisterResponse> =
        baseRequest {
            val request = RegisterRequestBody(
                username = data.username,
                email = data.email,
                password = data.password,
                repeatPassword = data.password
            )

            api.register(request)
        }

    override suspend fun login(data: LoginData): NetworkResponse<LoginResponse> = baseRequest {
        val requestBody = LoginRequestBody(
            email = data.email,
            password = data.password,
        )

        api.token(requestBody)
    }

    override suspend fun getUserPath(token: String): NetworkResponse<UserPathResponse> =
        baseRequest {
            api.getUserPath(token)
        }

    override suspend fun restorePassword(email: String): NetworkResponse<Any> =
        baseRequest { api.restorePassword(email) }
}
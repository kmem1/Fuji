package com.clownteam.authorization_datasource.network

import com.clownteam.authorization_datasource.network.login.LoginRequest
import com.clownteam.authorization_datasource.network.login.LoginResponse
import com.clownteam.authorization_datasource.network.register.RegisterRequest
import com.clownteam.authorization_datasource.network.register.RegisterResponse
import com.clownteam.authorization_domain.login.LoginData
import com.clownteam.authorization_domain.registration.RegistrationData
import com.clownteam.core.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("BlockingMethodInNonBlockingContext")
class AuthorizationServiceImpl(private val api: AuthorizationApi) : AuthorizationService {

    override suspend fun register(data: RegistrationData): NetworkResponse<RegisterResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = RegisterRequest(
                    username = data.username,
                    email = data.email,
                    password = data.password,
                    repeatPassword = data.password
                )

                val apiResponse = api.register(request).execute()

                NetworkResponse(statusCode = apiResponse.code(), data = apiResponse.body())
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse(
                    isNetworkError = true,
                    data = null,
                    errorMessage = e.message ?: ""
                )
            }
        }

    override suspend fun login(data: LoginData): NetworkResponse<LoginResponse> =
        withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(
                    email = data.email,
                    password = data.password,
                )

                val apiResponse = api.token(request).execute()

                NetworkResponse(statusCode = apiResponse.code(), data = apiResponse.body())
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse(
                    isNetworkError = true,
                    data = null,
                    errorMessage = e.message ?: ""
                )
            }
        }
}
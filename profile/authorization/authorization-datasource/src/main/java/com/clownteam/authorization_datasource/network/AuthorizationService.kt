package com.clownteam.authorization_datasource.network

import com.clownteam.authorization_datasource.network.login.LoginResponse
import com.clownteam.authorization_datasource.network.register.RegisterResponse
import com.clownteam.authorization_datasource.network.user_path.UserPathResponse
import com.clownteam.authorization_domain.login.LoginData
import com.clownteam.authorization_domain.registration.RegistrationData
import com.clownteam.core.network.NetworkResponse

interface AuthorizationService {

    suspend fun register(data: RegistrationData): NetworkResponse<RegisterResponse>

    suspend fun login(data: LoginData): NetworkResponse<LoginResponse>

    suspend fun getUserPath(token: String): NetworkResponse<UserPathResponse>

    suspend fun restorePassword(email: String): NetworkResponse<Any>
}
package com.clownteam.fuji.di.interactors.authorization

import com.clownteam.authorization_datasource.network.AuthorizationService
import com.clownteam.authorization_domain.registration.RegistrationData
import com.clownteam.fuji.api.FujiService
import com.clownteam.fuji.api.register.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorizationServiceImpl(private val fujiService: FujiService) : AuthorizationService {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun register(data: RegistrationData): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = RegisterRequest(
                username = data.username,
                email = data.email,
                password = data.password,
                repeatPassword = data.password
            )

            fujiService.register(request).execute().isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
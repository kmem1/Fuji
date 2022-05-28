package com.clownteam.authorization_datasource.network

import com.clownteam.authorization_domain.registration.RegistrationData

interface AuthorizationService {

    suspend fun register(data: RegistrationData): Boolean
}
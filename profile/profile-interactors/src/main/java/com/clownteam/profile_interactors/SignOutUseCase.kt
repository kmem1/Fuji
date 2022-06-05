package com.clownteam.profile_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

internal class SignOutUseCase(
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager
) : ISignOutUseCase {

    override suspend fun invoke() {
        tokenManager.clearTokens()
        userDataManager.clearData()
    }
}

interface ISignOutUseCase : IUseCase.Invokable
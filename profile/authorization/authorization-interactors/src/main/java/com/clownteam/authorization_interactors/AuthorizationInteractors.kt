package com.clownteam.authorization_interactors

import com.clownteam.authorization_datasource.network.AuthorizationApi
import com.clownteam.authorization_datasource.network.AuthorizationServiceImpl
import com.clownteam.core.interactors.IValidateLoginUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager

class AuthorizationInteractors private constructor(
    val validateLogin: IValidateLoginUseCase,
    val validateEmail: IValidateEmailUseCase,
    val validatePassword: IValidatePasswordUseCase,
    val validateRepeatedPassword: IValidateRepeatedPasswordUseCase,
    val register: IRegistrationUseCase,
    val login: ILoginUseCase,
    val restorePassword: IRestorePasswordUseCase
) {
    companion object Factory {
        fun build(
            authorizationApi: AuthorizationApi,
            tokenManager: TokenManager,
            userDataManager: UserDataManager
        ): AuthorizationInteractors {
            val authorizationService = AuthorizationServiceImpl(authorizationApi)
            return AuthorizationInteractors(
                IValidateLoginUseCase.create(),
                ValidateEmailUseCase(),
                ValidatePasswordUseCase(),
                ValidateRepeatedPasswordUseCase(),
                RegistrationUseCase(authorizationService),
                LoginUseCase(authorizationService, tokenManager, userDataManager),
                RestorePasswordUseCase(authorizationService)
            )
        }
    }
}
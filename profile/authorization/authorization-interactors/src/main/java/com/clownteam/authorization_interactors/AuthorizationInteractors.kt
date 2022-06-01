package com.clownteam.authorization_interactors

import com.clownteam.authorization_datasource.network.AuthorizationApi
import com.clownteam.authorization_datasource.network.AuthorizationServiceImpl

class AuthorizationInteractors private constructor(
    val validateLogin: IValidateLoginUseCase,
    val validateEmail: IValidateEmailUseCase,
    val validatePassword: IValidatePasswordUseCase,
    val validateRepeatedPassword: IValidateRepeatedPasswordUseCase,
    val register: IRegistrationUseCase,
    val login: ILoginUseCase
){
  companion object Factory {
      fun build(authorizationApi: AuthorizationApi): AuthorizationInteractors {
          val authorizationService = AuthorizationServiceImpl(authorizationApi)
          return AuthorizationInteractors(
              ValidateLoginUseCase(),
              ValidateEmailUseCase(),
              ValidatePasswordUseCase(),
              ValidateRepeatedPasswordUseCase(),
              RegistrationUseCase(authorizationService),
              LoginUseCase(authorizationService)
          )
      }
  }
}
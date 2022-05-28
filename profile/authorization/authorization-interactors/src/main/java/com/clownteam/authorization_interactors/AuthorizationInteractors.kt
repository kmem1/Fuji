package com.clownteam.authorization_interactors

import com.clownteam.authorization_datasource.network.AuthorizationService

class AuthorizationInteractors private constructor(
    val validateLogin: IValidateLoginUseCase,
    val validateEmail: IValidateEmailUseCase,
    val validatePassword: IValidatePasswordUseCase,
    val validateRepeatedPassword: IValidateRepeatedPasswordUseCase,
    val register: IRegisterUseCase
){
  companion object Factory {
      fun build(authorizationService: AuthorizationService): AuthorizationInteractors {
          return AuthorizationInteractors(
              ValidateLoginUseCase(),
              ValidateEmailUseCase(),
              ValidatePasswordUseCase(),
              ValidateRepeatedPasswordUseCase(),
              RegisterUseCase(authorizationService)
          )
      }
  }
}
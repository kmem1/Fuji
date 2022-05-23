package com.clownteam.authorization_interactors

class AuthorizationInteractors private constructor(
    val validateLogin: IValidateLoginUseCase,
    val validateEmail: IValidateEmailUseCase,
    val validatePassword: IValidatePasswordUseCase,
    val validateRepeatedPassword: IValidateRepeatedPasswordUseCase
){
  companion object Factory {
      fun build(): AuthorizationInteractors {
          return AuthorizationInteractors(
              ValidateLoginUseCase(),
              ValidateEmailUseCase(),
              ValidatePasswordUseCase(),
              ValidateRepeatedPasswordUseCase()
          )
      }
  }
}
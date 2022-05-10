package com.clownteam.authorization_interactors

class AuthorizationInteractors private constructor(
    val validateEmail: IValidateEmailUseCase,
    val validatePassword: IValidatePasswordUseCase
){
  companion object Factory {
      fun build(): AuthorizationInteractors {
          return AuthorizationInteractors(
              ValidateEmailUseCase(),
              ValidatePasswordUseCase()
          )
      }
  }
}
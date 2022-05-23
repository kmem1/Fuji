package com.clownteam.ui_authorization.di

import com.clownteam.authorization_interactors.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthorizationModule {

    @Singleton
    @Provides
    fun provideValidateLoginUseCase(interactors: AuthorizationInteractors): IValidateLoginUseCase {
        return interactors.validateLogin
    }

    @Singleton
    @Provides
    fun provideValidateEmailUseCase(interactors: AuthorizationInteractors): IValidateEmailUseCase {
        return interactors.validateEmail
    }

    @Singleton
    @Provides
    fun provideValidatePasswordUseCase(interactors: AuthorizationInteractors): IValidatePasswordUseCase {
        return interactors.validatePassword
    }

    @Singleton
    @Provides
    fun provideValidateRepeatedPasswordUseCase(interactors: AuthorizationInteractors): IValidateRepeatedPasswordUseCase {
        return interactors.validateRepeatedPassword
    }
}
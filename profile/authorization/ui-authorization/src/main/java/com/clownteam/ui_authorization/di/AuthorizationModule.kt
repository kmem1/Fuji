package com.clownteam.ui_authorization.di

import com.clownteam.authorization_interactors.AuthorizationInteractors
import com.clownteam.authorization_interactors.IValidateEmailUseCase
import com.clownteam.authorization_interactors.IValidatePasswordUseCase
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
    fun provideValidateEmailUseCase(interactors: AuthorizationInteractors): IValidateEmailUseCase {
        return interactors.validateEmail
    }

    @Singleton
    @Provides
    fun provideValidatePasswordUseCase(interactors: AuthorizationInteractors): IValidatePasswordUseCase {
        return interactors.validatePassword
    }
}
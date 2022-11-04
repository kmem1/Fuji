package com.clownteam.profile_interactors

import com.clownteam.core.interactors.IValidateLoginUseCase
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.profile_datasource.network.ProfileApi
import com.clownteam.profile_datasource.network.ProfileServiceImpl

class ProfileInteractors private constructor(
    val getProfile: IGetProfileUseCase,
    val getProfileCourses: IGetProfileCoursesUseCase,
    val getProfileCollections: IGetProfileCollectionsUseCase,
    val signOut: ISignOutUseCase,
    val validateLogin: IValidateLoginUseCase
) {

    companion object Factory {

        fun build(
            profileApi: ProfileApi,
            tokenManager: TokenManager,
            userDataManager: UserDataManager,
            baseUrl: String
        ): ProfileInteractors {
            val profileService = ProfileServiceImpl(profileApi)

            return ProfileInteractors(
                getProfile = GetProfileUseCase(
                    profileService,
                    userDataManager,
                    tokenManager,
                    baseUrl
                ),
                getProfileCourses = GetProfileCoursesUseCase(
                    profileService,
                    userDataManager,
                    tokenManager,
                    baseUrl
                ),
                getProfileCollections = GetProfileCollectionsUseCase(
                    profileService,
                    userDataManager,
                    tokenManager,
                    baseUrl
                ),
                signOut = SignOutUseCase(tokenManager, userDataManager),
                validateLogin = IValidateLoginUseCase.create()
            )
        }
    }
}
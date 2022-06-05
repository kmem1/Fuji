package com.clownteam.profile_interactors

import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.profile_datasource.network.ProfileApi
import com.clownteam.profile_datasource.network.ProfileServiceImpl

class ProfileInteractors private constructor(
    val getProfile: IGetProfileUseCase,
    val signOut: ISignOutUseCase
) {

    companion object Factory {

        fun build(profileApi: ProfileApi, tokenManager: TokenManager, userDataManager: UserDataManager): ProfileInteractors {
            val profileService = ProfileServiceImpl(profileApi)

            return ProfileInteractors(
                GetProfileUseCase(profileService, tokenManager),
                SignOutUseCase(tokenManager, userDataManager)
            )
        }
    }
}
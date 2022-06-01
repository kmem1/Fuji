package com.clownteam.profile_interactors

import com.clownteam.profile_datasource.network.ProfileApi
import com.clownteam.profile_datasource.network.ProfileServiceImpl

class ProfileInteractors private constructor(val getProfile: IGetProfileUseCase) {

    companion object Factory {

        fun build(profileApi: ProfileApi): ProfileInteractors {
            val profileService = ProfileServiceImpl(profileApi)

            return ProfileInteractors(GetProfileUseCase(profileService))
        }
    }
}
package com.example.user_archive_interactors

import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.user_archive_datasource.UserArchiveApi
import com.clownteam.user_archive_datasource.UserArchiveServiceImpl

class UserArchiveInteractors(
    val getArchiveCourses: IGetArchiveCoursesUseCase,
    val getArchiveCollections: IGetArchiveCollectionsUseCase
) {

    companion object {
        fun build(
            api: UserArchiveApi,
            tokenManager: TokenManager,
            userDataManager: UserDataManager,
            baseUrl: String
        ): UserArchiveInteractors {
            val service = UserArchiveServiceImpl(api)

            return UserArchiveInteractors(
                GetArchiveCoursesUseCase(service, tokenManager, userDataManager, baseUrl),
                GetArchiveCollectionsUseCase(service, tokenManager, userDataManager, baseUrl)
            )
        }
    }
}
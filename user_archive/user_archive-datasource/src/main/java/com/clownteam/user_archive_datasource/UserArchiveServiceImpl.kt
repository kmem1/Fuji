package com.clownteam.user_archive_datasource

import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest
import com.clownteam.user_archive_datasource.models.get_collections.GetArchiveCollectionsResponse
import com.clownteam.user_archive_datasource.models.get_courses.GetArchiveCoursesResponse

class UserArchiveServiceImpl(private val api: UserArchiveApi) : UserArchiveService {

    override suspend fun getArchiveCourses(
        token: String,
        userPath: String,
        query: String,
        page: Int
    ): NetworkResponse<GetArchiveCoursesResponse> =
        baseRequest { api.getArchiveCourses(token, userPath, query, page) }

    override suspend fun getArchiveCollections(
        token: String,
        userPath: String,
        query: String,
        page: Int
    ): NetworkResponse<GetArchiveCollectionsResponse> =
        baseRequest { api.getArchiveCollections(token, userPath, query, page) }
}
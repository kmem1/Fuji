package com.clownteam.user_archive_datasource

import com.clownteam.core.network.NetworkResponse
import com.clownteam.user_archive_datasource.models.get_collections.GetArchiveCollectionsResponse
import com.clownteam.user_archive_datasource.models.get_courses.GetArchiveCoursesResponse

interface UserArchiveService {

    suspend fun getArchiveCourses(
        token: String,
        userPath: String,
        query: String,
        page: Int
    ): NetworkResponse<GetArchiveCoursesResponse>

    suspend fun getArchiveCollections(
        token: String,
        userPath: String,
        query: String,
        page: Int
    ): NetworkResponse<GetArchiveCollectionsResponse>
}
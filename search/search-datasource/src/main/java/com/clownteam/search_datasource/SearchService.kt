package com.clownteam.search_datasource

import com.clownteam.core.network.NetworkResponse
import com.clownteam.search_datasource.models.get_collections.GetCollectionsByQueryResponse
import com.clownteam.search_datasource.models.get_courses.GetCoursesByQueryResponse

interface SearchService {

    suspend fun getCoursesByQuery(
        token: String,
        query: String,
        page: Int
    ): NetworkResponse<GetCoursesByQueryResponse>

    suspend fun getCollectionsByQuery(
        token: String,
        query: String,
        page: Int
    ): NetworkResponse<GetCollectionsByQueryResponse>
}
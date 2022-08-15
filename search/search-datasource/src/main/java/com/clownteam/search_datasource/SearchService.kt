package com.clownteam.search_datasource

import com.clownteam.core.network.NetworkResponse
import com.clownteam.search_datasource.models.GetCoursesByQueryResponse

interface SearchService {

    suspend fun getCoursesByQuery(
        token: String,
        query: String
    ): NetworkResponse<GetCoursesByQueryResponse>
}
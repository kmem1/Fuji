package com.clownteam.search_datasource

import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest
import com.clownteam.search_datasource.models.GetCoursesByQueryResponse

class SearchServiceImpl(private val searchApi: SearchApi) : SearchService {

    override suspend fun getCoursesByQuery(
        token: String,
        query: String
    ): NetworkResponse<GetCoursesByQueryResponse> =
        baseRequest { searchApi.searchCoursesByQuery(token, query) }
}
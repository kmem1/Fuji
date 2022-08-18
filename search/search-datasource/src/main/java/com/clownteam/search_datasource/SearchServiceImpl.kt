package com.clownteam.search_datasource

import com.clownteam.core.network.NetworkResponse
import com.clownteam.core.network.baseRequest
import com.clownteam.search_datasource.models.get_collections.GetCollectionsByQueryResponse
import com.clownteam.search_datasource.models.get_courses.GetCoursesByQueryResponse

class SearchServiceImpl(private val searchApi: SearchApi) : SearchService {

    override suspend fun getCoursesByQuery(
        token: String,
        query: String,
        page: Int
    ): NetworkResponse<GetCoursesByQueryResponse> =
        baseRequest { searchApi.searchCoursesByQuery(token, query, page) }

    override suspend fun getCollectionsByQuery(
        token: String,
        query: String,
        page: Int
    ): NetworkResponse<GetCollectionsByQueryResponse> =
        baseRequest { searchApi.searchCollectionsByQuery(token, query, page) }

}
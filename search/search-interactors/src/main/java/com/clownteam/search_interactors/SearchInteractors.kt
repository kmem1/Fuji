package com.clownteam.search_interactors

import com.clownteam.core.network.token.TokenManager
import com.clownteam.search_datasource.SearchApi
import com.clownteam.search_datasource.SearchServiceImpl

class SearchInteractors(
    val getCoursesByQuery: IGetCoursesByQueryUseCase,
    val getCollectionsByQuery: IGetCollectionsByQueryUseCase
) {
    companion object Factory {
        fun build(
            api: SearchApi,
            baseUrl: String,
            tokenManager: TokenManager
        ): SearchInteractors {
            val service = SearchServiceImpl(api)
            return SearchInteractors(
                GetCoursesByQueryUseCase(service, tokenManager, baseUrl),
                GetCollectionsByQueryUseCase(service, tokenManager, baseUrl)
            )
        }
    }
}
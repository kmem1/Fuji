package com.clownteam.search_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.paging.PagingSourceData
import com.clownteam.search_datasource.SearchService
import com.clownteam.search_domain.SearchResultItem
import com.clownteam.search_interactors.mappers.CourseSearchResultsMapper

internal class GetCoursesByQueryUseCase(
    private val searchService: SearchService,
    val tokenManager: TokenManager,
    private val baseUrl: String
) : IGetCoursesByQueryUseCase {

    override suspend fun invoke(param: GetCoursesByQueryParams): GetCoursesByQueryUseCaseResult {
        val result = authorizationRequest(tokenManager) { token ->
            searchService.getCoursesByQuery(token, param.query, param.page)
        }

        if (result.isUnauthorized) GetCoursesByQueryUseCaseResult.Unauthorized
        if (result.isNetworkError) GetCoursesByQueryUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            if ((result.data?.pages ?: 0) < param.page) {
                return GetCoursesByQueryUseCaseResult.Success(PagingSourceData.empty())
            }

            result.data?.results?.let {
                val mappedResult = it.map { model -> CourseSearchResultsMapper.map(model, baseUrl) }
                val hasNextPage = result.data?.hasNextPage ?: false

                GetCoursesByQueryUseCaseResult.Success(PagingSourceData(mappedResult, hasNextPage))
            } ?: GetCoursesByQueryUseCaseResult.Failed
        } else {
            GetCoursesByQueryUseCaseResult.Failed
        }
    }
}

interface IGetCoursesByQueryUseCase :
    IUseCase.InOut<GetCoursesByQueryParams, GetCoursesByQueryUseCaseResult>

data class GetCoursesByQueryParams(
    val query: String,
    val page: Int
)

sealed class GetCoursesByQueryUseCaseResult {
    class Success(val pagingData: PagingSourceData<SearchResultItem.Course>) :
        GetCoursesByQueryUseCaseResult()

    object Failed : GetCoursesByQueryUseCaseResult()

    object Unauthorized : GetCoursesByQueryUseCaseResult()

    object NetworkError : GetCoursesByQueryUseCaseResult()
}
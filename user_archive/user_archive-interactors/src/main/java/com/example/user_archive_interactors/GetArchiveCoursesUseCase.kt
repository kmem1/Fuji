package com.example.user_archive_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.paging.PagingSourceData
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.user_archive_datasource.UserArchiveService
import com.clownteam.user_archive_domain.ArchiveItem
import com.example.user_archive_interactors.mappers.ArchiveCoursesMapper

internal class GetArchiveCoursesUseCase(
    private val service: UserArchiveService,
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager,
    private val baseUrl: String
) : IGetArchiveCoursesUseCase {

    override suspend fun invoke(param: GetArchiveCoursesParams): GetArchiveCoursesUseCaseResult {
        val userPath = userDataManager.getUserPath() ?: ""

        val result = authorizationRequest(tokenManager) { token ->
            service.getArchiveCourses(token, userPath, param.query, param.page)
        }

        if (result.isUnauthorized) GetArchiveCoursesUseCaseResult.Unauthorized
        if (result.isNetworkError) GetArchiveCoursesUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            if ((result.data?.pages ?: 0) < param.page) {
                return GetArchiveCoursesUseCaseResult.Success(PagingSourceData.empty())
            }

            result.data?.results?.let {
                val mappedResult =
                    it.map { model -> ArchiveCoursesMapper.map(model, baseUrl) }
                val hasNextPage = result.data?.hasNextPage ?: false
                val pagingData = PagingSourceData(mappedResult, hasNextPage)

                GetArchiveCoursesUseCaseResult.Success(pagingData)
            } ?: GetArchiveCoursesUseCaseResult.Failed
        } else {
            GetArchiveCoursesUseCaseResult.Failed
        }
    }
}

interface IGetArchiveCoursesUseCase :
    IUseCase.InOut<GetArchiveCoursesParams, GetArchiveCoursesUseCaseResult>

data class GetArchiveCoursesParams(
    val query: String,
    val page: Int
)

sealed class GetArchiveCoursesUseCaseResult {
    class Success(val pagingData: PagingSourceData<ArchiveItem.Course>) :
        GetArchiveCoursesUseCaseResult()

    object Failed : GetArchiveCoursesUseCaseResult()

    object Unauthorized : GetArchiveCoursesUseCaseResult()

    object NetworkError : GetArchiveCoursesUseCaseResult()
}
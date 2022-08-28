package com.example.user_archive_interactors

import com.clownteam.core.domain.IUseCase
import com.clownteam.core.network.authorizationRequest
import com.clownteam.core.network.token.TokenManager
import com.clownteam.core.paging.PagingSourceData
import com.clownteam.core.user_data.UserDataManager
import com.clownteam.user_archive_datasource.UserArchiveService
import com.clownteam.user_archive_domain.ArchiveItem
import com.example.user_archive_interactors.mappers.ArchiveCollectionsMapper

internal class GetArchiveCollectionsUseCase(
    private val service: UserArchiveService,
    private val tokenManager: TokenManager,
    private val userDataManager: UserDataManager,
    private val baseUrl: String
) : IGetArchiveCollectionsUseCase {

    override suspend fun invoke(param: GetArchiveCollectionsParams): GetArchiveCollectionsUseCaseResult {
        val userPath = userDataManager.getUserPath() ?: ""

        val result = authorizationRequest(tokenManager) { token ->
            service.getArchiveCollections(token, userPath, param.query, param.page)
        }

        if (result.isUnauthorized) GetArchiveCollectionsUseCaseResult.Unauthorized
        if (result.isNetworkError) GetArchiveCollectionsUseCaseResult.NetworkError

        return if (result.isSuccessCode && result.data != null) {
            if ((result.data?.pages ?: 0) < param.page) {
                return GetArchiveCollectionsUseCaseResult.Success(PagingSourceData.empty())
            }

            result.data?.results?.let {
                val mappedResult =
                    it.map { model -> ArchiveCollectionsMapper.map(model, baseUrl) }
                val hasNextPage = result.data?.hasNextPage ?: false
                val pagingData = PagingSourceData(mappedResult, hasNextPage)

                GetArchiveCollectionsUseCaseResult.Success(pagingData)
            } ?: GetArchiveCollectionsUseCaseResult.Failed
        } else {
            GetArchiveCollectionsUseCaseResult.Failed
        }
    }
}

interface IGetArchiveCollectionsUseCase :
    IUseCase.InOut<GetArchiveCollectionsParams, GetArchiveCollectionsUseCaseResult>

data class GetArchiveCollectionsParams(
    val query: String,
    val page: Int
)

sealed class GetArchiveCollectionsUseCaseResult {
    class Success(val pagingData: PagingSourceData<ArchiveItem.Collection>) :
        GetArchiveCollectionsUseCaseResult()

    object Failed : GetArchiveCollectionsUseCaseResult()

    object Unauthorized : GetArchiveCollectionsUseCaseResult()

    object NetworkError : GetArchiveCollectionsUseCaseResult()
}
package com.clownteam.ui_search.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.clownteam.components.UiText
import com.clownteam.components.paging.DefaultPagingSource
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.paging.PagingSourceData
import com.clownteam.search_domain.SearchResultItem
import com.clownteam.search_interactors.*
import com.clownteam.ui_search.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getCoursesByQueryUseCase: IGetCoursesByQueryUseCase,
    private val getCollectionsByQueryUseCase: IGetCollectionsByQueryUseCase
) : ViewModel(), EventHandler<SearchEvent> {

    companion object {
        private const val SEARCH_DELAY_MS = 200L
    }

    var state by mutableStateOf(SearchState())

    private var isCoursesRequestLoading = false
    private var isCollectionsRequestLoading = false

    private val courseSource: DefaultPagingSource<SearchResultItem.Course>
        get() {
            return DefaultPagingSource { page ->
                Log.d("Kmem", "Courses: getNewItems")

                if (state.query.isEmpty()) return@DefaultPagingSource PagingSourceData.empty()

                isCoursesRequestLoading = true

                val result =
                    getCoursesByQueryUseCase.invoke(
                        GetCoursesByQueryParams(
                            query = state.query,
                            page
                        )
                    )

                Log.d("Kmem", "Courses result: $result")

                isCoursesRequestLoading = false
                checkLoadingRequests()

                when (result) {
                    is GetCoursesByQueryUseCaseResult.Success -> {
                        Log.d(
                            "Kmem",
                            "Courses itemsCount: ${result.pagingData.data?.size} page: $page"
                        )
                        result.pagingData
                    }
                    else -> {
                        handleGetCoursesByQueryUseCaseResult(result)
                        PagingSourceData.failed()
                    }
                }
            }
        }

    var coursesFlow by mutableStateOf(
        Pager(PagingConfig(pageSize = 10)) {
            courseSource
        }.flow
    )

    private val collectionSource: DefaultPagingSource<SearchResultItem.Collection>
        get() {
            return DefaultPagingSource { page ->
                Log.d("Kmem", "Collections: getNewItems")

                if (state.query.isEmpty()) return@DefaultPagingSource PagingSourceData.empty()

                isCollectionsRequestLoading = true

                val result =
                    getCollectionsByQueryUseCase.invoke(
                        GetCollectionsByQueryParams(
                            query = state.query,
                            page
                        )
                    )

                Log.d("Kmem", "Collections result: $result")

                isCollectionsRequestLoading = false
                checkLoadingRequests()

                when (result) {
                    is GetCollectionsByQueryUseCaseResult.Success -> {
                        Log.d(
                            "Kmem",
                            "Collections itemsCount: ${result.pagingData.data?.size} page: $page"
                        )
                        result.pagingData
                    }
                    else -> {
                        handleGetCollectionsByQueryUseCaseResult(result)
                        PagingSourceData.failed()
                    }
                }
            }
        }

    var collectionsFlow by mutableStateOf(
        Pager(PagingConfig(pageSize = 10)) {
            collectionSource
        }.flow
    )

    private fun checkLoadingRequests() {
        if (!isCoursesRequestLoading && !isCollectionsRequestLoading && state.isLoading) {
            state = state.copy(isLoading = false)
        }
    }

    private var searchJob: Job? = null

    override fun obtainEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.SetQuery -> {
                state = state.copy(
                    query = event.query,
                    isLoading = event.query.isNotEmpty(),
                    shouldSearchItems = false
                )

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(SEARCH_DELAY_MS)
                    state = state.copy(shouldSearchItems = true)
                }
            }
        }
    }

    private fun handleGetCoursesByQueryUseCaseResult(result: GetCoursesByQueryUseCaseResult) {
        when (result) {
            GetCoursesByQueryUseCaseResult.Failed -> {
                onFailedResult()
            }

            GetCoursesByQueryUseCaseResult.NetworkError -> {
                onNetworkError()
            }

            is GetCoursesByQueryUseCaseResult.Success -> {
                state = state.copy(errorMessage = null, isUnauthorized = false)
            }

            GetCoursesByQueryUseCaseResult.Unauthorized -> {
                onUnauthorized()
            }
        }

        setLoadingState(false)
    }

    private fun handleGetCollectionsByQueryUseCaseResult(result: GetCollectionsByQueryUseCaseResult) {
        when (result) {
            GetCollectionsByQueryUseCaseResult.Failed -> {
                onFailedResult()
            }

            GetCollectionsByQueryUseCaseResult.NetworkError -> {
                onNetworkError()
            }

            is GetCollectionsByQueryUseCaseResult.Success -> {
                state = state.copy(errorMessage = null, isUnauthorized = false)
            }

            GetCollectionsByQueryUseCaseResult.Unauthorized -> {
                onUnauthorized()
            }
        }

        setLoadingState(false)
    }

    private fun onFailedResult() {
        state = state.copy(errorMessage = UiText.StringResource(R.string.on_failed_search_message))
    }

    private fun onNetworkError() {
        state = state.copy(errorMessage = UiText.StringResource(R.string.network_error_message))
    }

    private fun onUnauthorized() {
        state = state.copy(isUnauthorized = true)
    }

    private fun setLoadingState(isLoading: Boolean) {
        state = state.copy(isLoading = isLoading)
    }
}
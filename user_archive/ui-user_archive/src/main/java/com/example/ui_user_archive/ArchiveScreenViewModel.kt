package com.example.ui_user_archive

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.clownteam.components.UiText
import com.clownteam.components.paging.DefaultPagingSource
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.core.paging.PagingSourceData
import com.clownteam.user_archive_domain.ArchiveItem
import com.example.user_archive_interactors.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveScreenViewModel @Inject constructor(
    private val getArchiveCourses: IGetArchiveCoursesUseCase,
    private val getArchiveCollections: IGetArchiveCollectionsUseCase
) : ViewModel(), EventHandler<ArchiveScreenEvent>,
    StateHolder<MutableState<ArchiveScreenState>> {

    companion object {
        private const val SEARCH_DELAY_MS = 200L
    }

    override val state: MutableState<ArchiveScreenState> = mutableStateOf(ArchiveScreenState())

    private var isCoursesRequestLoading = false
    private var isCollectionsRequestLoading = false

    private val courseSource: DefaultPagingSource<ArchiveItem.Course>
        get() {
            return DefaultPagingSource { page ->
                Log.d("Kmem", "Courses: getNewItems")

                isCoursesRequestLoading = true

                val result = getArchiveCourses.invoke(
                    GetArchiveCoursesParams(
                        query = state.value.query,
                        page
                    )
                )

                Log.d("Kmem", "Courses result: $result")

                isCoursesRequestLoading = false
                checkLoadingRequests()

                when (result) {
                    is GetArchiveCoursesUseCaseResult.Success -> {
                        Log.d(
                            "Kmem",
                            "Courses itemsCount: ${result.pagingData.data?.size} page: $page"
                        )
                        result.pagingData
                    }
                    else -> {
                        handleResult(result)
                        PagingSourceData.failed()
                    }
                }
            }
        }

    var coursesFlow = Pager(PagingConfig(pageSize = 10)) { courseSource }.flow

    private val collectionSource: DefaultPagingSource<ArchiveItem.Collection>
        get() {
            return DefaultPagingSource { page ->
                Log.d("Kmem", "Collections: getNewItems")

                isCollectionsRequestLoading = true

                val result =
                    getArchiveCollections.invoke(
                        GetArchiveCollectionsParams(
                            query = state.value.query,
                            page
                        )
                    )

                Log.d("Kmem", "Collections result: $result")

                isCollectionsRequestLoading = false
                checkLoadingRequests()

                when (result) {
                    is GetArchiveCollectionsUseCaseResult.Success -> {
                        Log.d(
                            "Kmem",
                            "Collections itemsCount: ${result.pagingData.data?.size} page: $page"
                        )
                        result.pagingData
                    }
                    else -> {
                        handleResult(result)
                        PagingSourceData.failed()
                    }
                }
            }
        }

    var collectionsFlow = Pager(PagingConfig(pageSize = 10)) { collectionSource }.flow

    private fun checkLoadingRequests() {
        if (!isCoursesRequestLoading && !isCollectionsRequestLoading && state.value.isLoading) {
            state.value = state.value.copy(isLoading = false)
        }
    }

    private var searchJob: Job? = null

    override fun obtainEvent(event: ArchiveScreenEvent) {
        when (event) {
            is ArchiveScreenEvent.SetQuery -> {
                if (event.query == state.value.query) return

                state.value = state.value.copy(
                    query = event.query,
                    isLoading = event.query.isNotEmpty(),
                    shouldSearchItems = false
                )

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(SEARCH_DELAY_MS)
                    state.value = state.value.copy(shouldSearchItems = true)
                }
            }
        }
    }

    private fun handleResult(result: GetArchiveCoursesUseCaseResult) {
        when (result) {
            GetArchiveCoursesUseCaseResult.Failed -> {
                state.value = state.value.copy(
                    errorMessage = UiText.StringResource(R.string.get_archive_items_failed_message)
                )
            }

            GetArchiveCoursesUseCaseResult.NetworkError -> {
                state.value = state.value.copy(
                    errorMessage = UiText.StringResource(R.string.network_error_message)
                )
            }

            is GetArchiveCoursesUseCaseResult.Success -> {
                state.value = state.value.copy(errorMessage = null, isUnauthorized = false)
            }

            GetArchiveCoursesUseCaseResult.Unauthorized -> {
                state.value = state.value.copy(isUnauthorized = true)
            }
        }

        state.value = state.value.copy(isLoading = false)
    }

    private fun handleResult(result: GetArchiveCollectionsUseCaseResult) {
        when (result) {
            GetArchiveCollectionsUseCaseResult.Failed -> {
                state.value = state.value.copy(
                    errorMessage = UiText.StringResource(R.string.get_archive_items_failed_message)
                )
            }

            GetArchiveCollectionsUseCaseResult.NetworkError -> {
                state.value = state.value.copy(
                    errorMessage = UiText.StringResource(R.string.network_error_message)
                )
            }

            is GetArchiveCollectionsUseCaseResult.Success -> {
                state.value = state.value.copy(errorMessage = null, isUnauthorized = false)
            }

            GetArchiveCollectionsUseCaseResult.Unauthorized -> {
                state.value = state.value.copy(isUnauthorized = true)
            }
        }

        state.value = state.value.copy(isLoading = false)
    }
}

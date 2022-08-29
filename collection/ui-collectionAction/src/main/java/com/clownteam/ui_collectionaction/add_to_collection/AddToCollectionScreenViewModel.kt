package com.clownteam.ui_collectionaction.add_to_collection

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.clownteam.collection_domain.CollectionSortOption
import com.clownteam.collection_domain.CourseCollection
import com.clownteam.collection_interactors.*
import com.clownteam.components.UiText
import com.clownteam.components.paging.DefaultPagingSource
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.core.paging.PagingSourceData
import com.clownteam.ui_collectionaction.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToCollectionScreenViewModel @Inject constructor(
    private val getUserCollections: IGetUserCollectionsUseCase,
    private val addCourseToCollectionUseCase: IAddCourseToCollectionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), EventHandler<AddToCollectionScreenEvent>,
    StateHolder<MutableState<AddToCollectionScreenState>> {

    companion object {
        const val COURSE_ID_ARG_KEY = "addToCollection_COURSE_ID_KEY"
        private const val SEARCH_DELAY_MS = 200L
    }

    private val courseId = savedStateHandle.get<String>(COURSE_ID_ARG_KEY)

    override val state = mutableStateOf(AddToCollectionScreenState(courseId = courseId ?: ""))

    private val collectionSource: DefaultPagingSource<CourseCollection>
        get() {
            return DefaultPagingSource { page ->
                val result =
                    getUserCollections.invoke(
                        GetUserCollectionsParams(
                            query = state.value.searchQuery,
                            page = page,
                            sortOption = state.value.sortOption
                        )
                    )

                updateState(state.value.copy(isCollectionListLoading = false))

                when (result) {
                    is GetUserCollectionsUseCaseResult.Success -> {
                        result.pagingData
                    }
                    else -> {
                        handleGetUserCollectionsResult(result)
                        PagingSourceData.failed()
                    }
                }
            }
        }

    val collectionsFlow = Pager(PagingConfig(pageSize = 40)) { collectionSource }.flow

    private var searchJob: Job? = null

    override fun obtainEvent(event: AddToCollectionScreenEvent) {
        when (event) {
            is AddToCollectionScreenEvent.AddToCollection -> {
                addCourseToCollection(event.collection.id)
            }

            is AddToCollectionScreenEvent.SetSearchQuery -> {
                updateState(
                    state.value.copy(
                        searchQuery = event.query,
                        shouldSearchItems = false,
                        isCollectionListLoading = true
                    )
                )

                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(SEARCH_DELAY_MS)
                    updateState(state.value.copy(shouldSearchItems = true))
                }
            }

            AddToCollectionScreenEvent.ErrorMessageShown -> {
                updateState(state.value.copy(addToCollectionErrorMessage = null))
            }

            is AddToCollectionScreenEvent.SetSortOption -> {
                setNewSortOption(event.sortOption)
            }
        }
    }

    private fun setNewSortOption(sortOption: CollectionSortOption) {
        // Create new object
        val newOption = if (sortOption.type == state.value.sortOption.type) {
            val currentDirection = state.value.sortOption.direction
            state.value.sortOption.copy(direction = currentDirection.reverse())
        } else {
            sortOption
        }

        updateState(
            state.value.copy(
                sortOption = newOption,
                isCollectionListLoading = true,
                shouldSearchItems = true
            )
        )
    }

    private fun addCourseToCollection(collectionId: String) {
        viewModelScope.launch {
            updateState(state.value.copy(isLoading = true))

            val params = AddCourseToCollectionParams(courseId ?: "", collectionId)
            val result = addCourseToCollectionUseCase.invoke(params)

            handleAddCourseToCollectionResult(result)
        }
    }

    private fun handleAddCourseToCollectionResult(result: AddCourseToCollectionUseCaseResult) {
        when (result) {
            AddCourseToCollectionUseCaseResult.Failed -> {
                updateState(
                    state.value.copy(
                        addToCollectionErrorMessage = UiText.StringResource(
                            R.string.add_to_collection_error_message
                        )
                    )
                )
            }

            AddCourseToCollectionUseCaseResult.NetworkError -> {
                updateState(
                    state.value.copy(
                        addToCollectionErrorMessage = UiText.StringResource(
                            R.string.network_error_message
                        )
                    )
                )
            }

            AddCourseToCollectionUseCaseResult.Success -> {
                updateState(state.value.copy(isSuccess = true))
            }

            AddCourseToCollectionUseCaseResult.Unauthorized -> {
                updateState(state.value.copy(isUnauthorized = true))
            }
        }

        updateState(state.value.copy(isLoading = false))
    }

    private fun handleGetUserCollectionsResult(result: GetUserCollectionsUseCaseResult) {
        when (result) {
            GetUserCollectionsUseCaseResult.Failed -> {
                updateState(
                    state.value.copy(
                        getCollectionsErrorMessage = UiText.StringResource(
                            R.string.get_user_collections_failed_message
                        )
                    )
                )
            }

            GetUserCollectionsUseCaseResult.NetworkError -> {
                updateState(
                    state.value.copy(
                        getCollectionsErrorMessage = UiText.StringResource(
                            R.string.network_error_message
                        )
                    )
                )
            }

            is GetUserCollectionsUseCaseResult.Success -> {
                updateState(state.value.copy(getCollectionsErrorMessage = null))
            }

            GetUserCollectionsUseCaseResult.Unauthorized -> {
                updateState(state.value.copy(isUnauthorized = true))
            }
        }
    }

    private fun updateState(newState: AddToCollectionScreenState) {
        state.value = newState
    }
}

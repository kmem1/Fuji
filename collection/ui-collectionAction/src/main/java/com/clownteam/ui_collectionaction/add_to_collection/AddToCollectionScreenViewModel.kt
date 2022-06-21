package com.clownteam.ui_collectionaction.add_to_collection

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.collection_interactors.*
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
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
    }

    private val courseId = savedStateHandle.get<String>(COURSE_ID_ARG_KEY)

    init {
        obtainEvent(AddToCollectionScreenEvent.GetMyCollections)
    }

    override val state: MutableState<AddToCollectionScreenState> =
        mutableStateOf(AddToCollectionScreenState.Loading)

    override fun obtainEvent(event: AddToCollectionScreenEvent) {
        when (event) {
            is AddToCollectionScreenEvent.AddToCollection -> {
                addCourseToCollection(event.collection.id)
            }

            AddToCollectionScreenEvent.GetMyCollections -> {
                getCollections()
            }
        }
    }

    private fun addCourseToCollection(collectionId: String) {
        viewModelScope.launch {
            updateState(AddToCollectionScreenState.Loading)

            val params = AddCourseToCollectionParams(courseId ?: "", collectionId)
            val result = addCourseToCollectionUseCase.invoke(params)

            handleAddCourseToCollectionResult(result)
        }
    }

    private fun handleAddCourseToCollectionResult(result: AddCourseToCollectionUseCaseResult) {
        when(result) {
            AddCourseToCollectionUseCaseResult.Failed -> {
                updateState(AddToCollectionScreenState.SuccessAddCourse)
            }

            AddCourseToCollectionUseCaseResult.NetworkError -> {
                updateState(AddToCollectionScreenState.Error)
            }

            AddCourseToCollectionUseCaseResult.Success -> {
                updateState(AddToCollectionScreenState.SuccessAddCourse)
            }

            AddCourseToCollectionUseCaseResult.Unauthorized -> {
                updateState(AddToCollectionScreenState.Unauthorized)
            }
        }
    }

    private fun getCollections() {
        viewModelScope.launch {
            val result = getUserCollections.invoke()

            handleGetUserCollectionsResult(result)
        }
    }

    private fun handleGetUserCollectionsResult(result: GetUserCollectionsUseCaseResult) {
        when (result) {
            GetUserCollectionsUseCaseResult.Failed -> {
                updateState(AddToCollectionScreenState.Error)
            }

            GetUserCollectionsUseCaseResult.NetworkError -> {
                updateState(AddToCollectionScreenState.Error)
            }

            is GetUserCollectionsUseCaseResult.Success -> {
                updateState(AddToCollectionScreenState.Data(result.data, courseId ?: ""))
            }

            GetUserCollectionsUseCaseResult.Unauthorized -> {
                updateState(AddToCollectionScreenState.Unauthorized)
            }
        }
    }

    private fun updateState(newState: AddToCollectionScreenState) {
        state.value = newState
    }
}

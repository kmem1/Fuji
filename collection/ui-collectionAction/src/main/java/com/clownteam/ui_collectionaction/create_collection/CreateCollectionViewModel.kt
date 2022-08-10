package com.clownteam.ui_collectionaction.create_collection

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.collection_interactors.*
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import com.clownteam.ui_collectionaction.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCollectionViewModel @Inject constructor(
    private val createCollectionUseCase: ICreateCollectionUseCase,
    private val updateCollectionUseCase: IUpdateCollectionUseCase,
    private val addCourseToCollectionUseCase: IAddCourseToCollectionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), EventHandler<CreateCollectionEvent>,
    StateHolder<MutableState<CreateCollectionState>> {

    companion object {
        const val COURSE_ID_ARG_KEY = "createCollection_COURSE_ID_ARG"
    }

    override val state: MutableState<CreateCollectionState> =
        mutableStateOf(CreateCollectionState())

    private val courseId = savedStateHandle.get<String>(COURSE_ID_ARG_KEY)

    override fun obtainEvent(event: CreateCollectionEvent) {
        when (event) {
            is CreateCollectionEvent.CreateCollection -> {
                createCollectionAndAddCourse(state.value.collectionTitle)
            }

            CreateCollectionEvent.ErrorMessageShown -> {
                state.value = state.value.copy(errorMessage = null)
            }

            is CreateCollectionEvent.TitleChanged -> {
                state.value = state.value.copy(collectionTitle = event.newTitle)
            }
        }
    }

    private fun createCollectionAndAddCourse(title: String) {
        viewModelScope.launch {
            state.value = state.value.copy(isLoading = true)

            val createCollectionResult = createCollectionUseCase.invoke()

            handleCreateCollectionResult(createCollectionResult, title)
        }
    }

    private fun handleCreateCollectionResult(
        result: CreateCollectionUseCaseResult,
        title: String
    ) {
        when (result) {
            CreateCollectionUseCaseResult.Failed -> {
                updateFailedState(isNetworkError = false)
            }

            CreateCollectionUseCaseResult.NetworkError -> {
                updateFailedState(isNetworkError = true)
            }

            is CreateCollectionUseCaseResult.Success -> {
                updateCollectionTitle(result.collectionId, title)
            }

            CreateCollectionUseCaseResult.Unauthorized -> {
                state.value = state.value.copy(isUnauthorized = true)
            }
        }
    }

    private fun updateCollectionTitle(collectionId: String, title: String) {
        viewModelScope.launch {
            val args = UpdateCollectionUseCaseArgs(collectionId, title)
            val result = updateCollectionUseCase.invoke(args)

            handleUpdateCollectionUseCaseResult(result, collectionId)
        }
    }

    private fun handleUpdateCollectionUseCaseResult(
        result: UpdateCollectionUseCaseResult,
        collectionId: String
    ) {
        when (result) {
            UpdateCollectionUseCaseResult.Failed -> {
                updateFailedState(isNetworkError = false)
            }

            UpdateCollectionUseCaseResult.NetworkError -> {
                updateFailedState(isNetworkError = true)
            }

            UpdateCollectionUseCaseResult.Success -> {
                addCourseToCollection(collectionId)
            }

            UpdateCollectionUseCaseResult.Unauthorized -> {
                updateUnauthorizedState()
            }
        }
    }

    private fun addCourseToCollection(collectionId: String) {
        viewModelScope.launch {
            if (courseId == null) return@launch

            val args = AddCourseToCollectionParams(courseId, collectionId)
            val result = addCourseToCollectionUseCase.invoke(args)

            handleAddCourseToCollectionUseCaseResult(result)
        }
    }

    private fun handleAddCourseToCollectionUseCaseResult(result: AddCourseToCollectionUseCaseResult) {
        when (result) {
            AddCourseToCollectionUseCaseResult.Failed -> {
                updateFailedState(isNetworkError = false)
            }

            AddCourseToCollectionUseCaseResult.NetworkError -> {
                updateFailedState(isNetworkError = true)
            }

            AddCourseToCollectionUseCaseResult.Success -> {
                updateSuccessState()
            }

            AddCourseToCollectionUseCaseResult.Unauthorized -> {
                updateUnauthorizedState()
            }
        }
    }

    private fun updateFailedState(isNetworkError: Boolean) {
        val errorMessage = if (isNetworkError) {
            UiText.StringResource(R.string.network_error_message)
        } else {
            UiText.StringResource(R.string.create_collection_failed_message)
        }

        state.value = state.value.copy(errorMessage = errorMessage)
    }

    private fun updateUnauthorizedState() {
        state.value = state.value.copy(isUnauthorized = true)
    }

    private fun updateSuccessState() {
        state.value = state.value.copy(isSuccess = true)
    }
}
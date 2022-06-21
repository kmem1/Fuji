package com.clownteam.ui_collectionaction.create_collection

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.collection_interactors.*
import com.clownteam.core.domain.EventHandler
import com.clownteam.core.domain.StateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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
        mutableStateOf(CreateCollectionState.Idle)

    private val courseId = savedStateHandle.get<String>(COURSE_ID_ARG_KEY)

    private val resultChannel = Channel<CreateCollectionResult>()
    val createResults = resultChannel.receiveAsFlow()

    override fun obtainEvent(event: CreateCollectionEvent) {
        when (event) {
            is CreateCollectionEvent.CreateCollection -> {
                createCollectionAndAddCourse(event.title)
            }
        }
    }

    private fun createCollectionAndAddCourse(title: String) {
        viewModelScope.launch {
            updateState(CreateCollectionState.Loading)

            val createCollectionResult = createCollectionUseCase.invoke()

            handleCreateCollectionResult(createCollectionResult, title)
        }
    }

    private suspend fun handleCreateCollectionResult(
        result: CreateCollectionUseCaseResult,
        title: String
    ) {
        when (result) {
            CreateCollectionUseCaseResult.Failed -> {
                resultChannel.send(CreateCollectionResult.Failed)
            }

            CreateCollectionUseCaseResult.NetworkError -> {
                resultChannel.send(CreateCollectionResult.NetworkError)
            }

            is CreateCollectionUseCaseResult.Success -> {
                updateCollectionTitle(result.collectionId, title)
            }

            CreateCollectionUseCaseResult.Unauthorized -> {
                resultChannel.send(CreateCollectionResult.Unauthorized)
            }
        }
    }

    private fun updateCollectionTitle(collectionId: String, title: String) {
        viewModelScope.launch {
            Log.d("Kmem", "collectionId: $collectionId")

            val args = UpdateCollectionUseCaseArgs(collectionId, title)
            val result = updateCollectionUseCase.invoke(args)

            handleUpdateCollectionUseCaseResult(result, collectionId)
        }
    }

    private suspend fun handleUpdateCollectionUseCaseResult(
        result: UpdateCollectionUseCaseResult,
        collectionId: String
    ) {
        when (result) {
            UpdateCollectionUseCaseResult.Failed -> {
                resultChannel.send(CreateCollectionResult.Failed)
            }

            UpdateCollectionUseCaseResult.NetworkError -> {
                resultChannel.send(CreateCollectionResult.NetworkError)
            }

            UpdateCollectionUseCaseResult.Success -> {
                addCourseToCollection(collectionId)
            }

            UpdateCollectionUseCaseResult.Unauthorized -> {
                resultChannel.send(CreateCollectionResult.Unauthorized)
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

    private suspend fun handleAddCourseToCollectionUseCaseResult(result: AddCourseToCollectionUseCaseResult) {
        when(result) {
            AddCourseToCollectionUseCaseResult.Failed -> {
                resultChannel.send(CreateCollectionResult.Failed)
            }

            AddCourseToCollectionUseCaseResult.NetworkError -> {
                resultChannel.send(CreateCollectionResult.NetworkError)
            }

            AddCourseToCollectionUseCaseResult.Success -> {
                resultChannel.send(CreateCollectionResult.Success)
                updateState(CreateCollectionState.Idle)
            }

            AddCourseToCollectionUseCaseResult.Unauthorized -> {
                resultChannel.send(CreateCollectionResult.Unauthorized)
            }
        }
    }

    private fun updateState(newState: CreateCollectionState) {
        state.value = newState
    }

    sealed class CreateCollectionResult {
        object Success : CreateCollectionResult()

        object Failed : CreateCollectionResult()

        object NetworkError : CreateCollectionResult()

        object Unauthorized : CreateCollectionResult()
    }
}
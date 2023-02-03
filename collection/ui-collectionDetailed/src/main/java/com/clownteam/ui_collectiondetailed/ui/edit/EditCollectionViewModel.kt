package com.clownteam.ui_collectiondetailed.ui.edit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clownteam.collection_interactors.*
import com.clownteam.components.UiText
import com.clownteam.core.domain.EventHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCollectionViewModel @Inject constructor(
    private val getCollectionUseCase: IGetCollectionUseCase,
    private val updateCollectionUseCase: IUpdateCollectionUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), EventHandler<EditCollectionScreenEvent> {

    var state by mutableStateOf(EditCollectionScreenState())

    private val collectionId = savedStateHandle.get<String>(COLLECTION_ID_ARG_KEY)

    init {
        if (collectionId == null) {
            state = state.copy(isError = true)
        } else {
            obtainEvent(EditCollectionScreenEvent.GetCollection)
        }
    }

    override fun obtainEvent(event: EditCollectionScreenEvent) {
        when (event) {
            is EditCollectionScreenEvent.SetDescription -> {
                state = state.copy(description = event.description)
            }

            is EditCollectionScreenEvent.SetTitle -> {
                state = state.copy(title = event.title)
            }

            is EditCollectionScreenEvent.SetImage -> {
                Log.d("Kmem", "${event.file.totalSpace}")
                state = state.copy(imageFile = event.file, imageFileBitmap = event.bitmap)
            }

            EditCollectionScreenEvent.ApplyChanges -> {
                tryToApplyChanges()
            }

            EditCollectionScreenEvent.MessageShown -> {
                state = state.copy(message = null)
            }

            EditCollectionScreenEvent.GetCollection -> {
                getCollectionData()
            }
        }
    }

    private fun getCollectionData() {
        viewModelScope.launch {
            if (collectionId == null) {
                state = state.copy(isError = true)
                return@launch
            }

            state = state.copy(isLoading = true)

            val collectionResult = getCollectionUseCase.invoke(collectionId)
            handleGetCollectionUseCaseResult(collectionResult)
        }
    }

    private fun handleGetCollectionUseCaseResult(result: GetCollectionUseCaseResult) {
        when (result) {
            GetCollectionUseCaseResult.Failed, GetCollectionUseCaseResult.NetworkError -> {
                state = state.copy(isError = true)
            }

            is GetCollectionUseCaseResult.Success -> {
                state = state.copy(
                    collectionImgUrl = result.data.imageUrl,
                    title = result.data.title,
                    description = result.data.description
                )
            }

            GetCollectionUseCaseResult.Unauthorized -> {
                state = state.copy(isUnauthorized = true)
            }
        }

        state = state.copy(isLoading = false)
    }

    private fun tryToApplyChanges() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, titleError = null, descriptionError = null)

            if (checkTitle()) {
                val updateCollectionResult = updateCollectionUseCase.invoke(
                    UpdateCollectionUseCaseArgs(
                        collectionId = collectionId ?: "",
                        newTitle = state.title,
                        newDescription = state.description,
                        newImage = state.imageFile
                    )
                )

                handleUpdateCollectionResult(updateCollectionResult)
            }

            state = state.copy(isLoading = false)
        }
    }

    private fun handleUpdateCollectionResult(result: UpdateCollectionUseCaseResult) {
        state = when (result) {
            UpdateCollectionUseCaseResult.Failed, UpdateCollectionUseCaseResult.NetworkError -> {
                state.copy(message = UiText.DynamicString("Ошибка при обновлении подборки"))
            }

            UpdateCollectionUseCaseResult.Success -> {
                state.copy(isSuccess = true)
            }

            UpdateCollectionUseCaseResult.Unauthorized -> {
                state.copy(isUnauthorized = true)
            }
        }

        state = state.copy(isLoading = false)
    }

    private fun checkTitle(): Boolean {
        if (state.title.isNotEmpty()) return true

        state = state.copy(titleError = UiText.DynamicString("Поле не может быть пустым"))
        return false
    }

    companion object {
        const val COLLECTION_ID_ARG_KEY = "collectionId"
    }
}

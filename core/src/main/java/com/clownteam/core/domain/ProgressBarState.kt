package com.clownteam.core.domain

sealed class ProgressBarState {

    object Loading: ProgressBarState()

    object Idle: ProgressBarState()
}
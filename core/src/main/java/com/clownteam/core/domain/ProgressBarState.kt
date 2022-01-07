package com.clownteam.core.domain

sealed class ProgressBarState {

    object Loading : ProgressBarState()

    object Idle : ProgressBarState()

    fun isIdle(): Boolean = this == Idle
    fun isNotIdle(): Boolean = this != Idle

    fun isLoading(): Boolean = this == Loading
    fun isNotLoading(): Boolean = this != Loading
}
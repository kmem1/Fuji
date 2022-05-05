package com.clownteam.core.domain

interface ISResult<out T : Any> {

    val data: T?

    var isNeedHandle: Boolean

    var isHandled: Boolean

    fun isSuccess(): Boolean

    fun isLoading(): Boolean

    fun isError(): Boolean

    fun isEmptySuccess(): Boolean

    fun handle(onHandled: () -> Unit)
}
package com.clownteam.core.network

import com.google.gson.annotations.SerializedName

open class PaginationResponse<T> {
    @SerializedName("count")
    val count: Int? = null

    @SerializedName("next")
    val next: String? = null

    @SerializedName("pages")
    val pages: Int? = null

    @SerializedName("previous")
    val previous: String? = null

    @SerializedName("results")
    val results: List<T>? = null

    val hasNextPage: Boolean
        get() = next != null && next.isNotEmpty()
}
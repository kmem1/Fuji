package com.clownteam.core.network

import com.google.gson.annotations.SerializedName

open class PaginationResponse<T> {
    @SerializedName("count")
    val count: Int? = null

    @SerializedName("next")
    val next: Int? = null

    @SerializedName("pages")
    val pages: Int? = null

    @SerializedName("previous")
    val previous: Int? = null

    @SerializedName("results")
    val results: List<T>? = null
}
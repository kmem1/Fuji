package com.clownteam.core.paging

data class PagingSourceData<T>(val data: List<T>?, val hasNextPage: Boolean) {

    companion object {
        fun <T> failed() = PagingSourceData<T>(data = null, hasNextPage = false)

        fun <T> empty() = PagingSourceData<T>(data = emptyList(), hasNextPage = false)
    }
}

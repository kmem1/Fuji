package com.clownteam.components.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.clownteam.core.paging.PagingSourceData

class DefaultPagingSource<T : Any>(
    val getNewItems: suspend (page: Int) -> PagingSourceData<T>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val nextPage = params.key ?: 1
        val pagingData = getNewItems(nextPage)
        val itemList = pagingData.data

        return if (itemList != null) {
            LoadResult.Page(
                data = itemList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (pagingData.hasNextPage) nextPage + 1 else null
            )
        } else {
            LoadResult.Error(Exception())
        }
    }
}

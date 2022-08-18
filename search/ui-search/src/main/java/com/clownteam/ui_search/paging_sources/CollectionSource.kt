package com.clownteam.ui_search.paging_sources

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.clownteam.search_domain.SearchResultItem

class CollectionSource(
    val getNewItems: suspend (page: Int) -> List<SearchResultItem.Collection>?
) : PagingSource<Int, SearchResultItem.Collection>() {

    override fun getRefreshKey(state: PagingState<Int, SearchResultItem.Collection>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResultItem.Collection> {
        val nextPage = params.key ?: 1
        val newList = getNewItems(nextPage)

        return if (newList != null) {
            LoadResult.Page(
                data = newList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (newList.size < 10) null else nextPage + 1
            )
        } else {
            LoadResult.Error(Exception())
        }
    }
}
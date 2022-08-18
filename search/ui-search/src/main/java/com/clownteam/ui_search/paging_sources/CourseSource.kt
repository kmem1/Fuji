package com.clownteam.ui_search.paging_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.clownteam.search_domain.SearchResultItem

class CourseSource(
    val getNewItems: suspend (page: Int) -> List<SearchResultItem.Course>?
) : PagingSource<Int, SearchResultItem.Course>() {

    override fun getRefreshKey(state: PagingState<Int, SearchResultItem.Course>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResultItem.Course> {
        val nextPage = params.key ?: 1
        val newList = getNewItems(nextPage)

        return if (newList != null) {
            LoadResult.Page(
                data = newList,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (newList.size < params.loadSize) null else nextPage + 1
            )
        } else {
            LoadResult.Error(Exception())
        }
    }
}
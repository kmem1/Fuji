package com.clownteam.ui_search.ui

import com.clownteam.components.UiText
import com.clownteam.search_domain.SearchFilter

data class SearchState(
    val isLoading: Boolean = false,
    val query: String = "",
    val shouldSearchItems: Boolean = false,
    val searchFilter: SearchFilter = SearchFilter.Courses,
    val errorMessage: UiText? = null,
    val isUnauthorized: Boolean = false
)
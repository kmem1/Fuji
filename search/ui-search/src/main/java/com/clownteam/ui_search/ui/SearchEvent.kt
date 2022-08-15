package com.clownteam.ui_search.ui

import com.clownteam.search_domain.SearchFilter

sealed class SearchEvent {

    class SetSearchFilter(val searchFilter: SearchFilter): SearchEvent()
    class SetQuery(val query: String): SearchEvent()
}
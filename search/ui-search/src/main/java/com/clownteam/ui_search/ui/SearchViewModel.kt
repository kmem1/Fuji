package com.clownteam.ui_search.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.clownteam.core.domain.EventHandler

class SearchViewModel: ViewModel(), EventHandler<SearchEvent> {

    var state by mutableStateOf(SearchState())

    override fun obtainEvent(event: SearchEvent) {
        when(event) {
            is SearchEvent.SetSearchFilter -> {
                state = state.copy(searchFilter = event.searchFilter)
            }

            is SearchEvent.SetQuery -> {
                state = state.copy(query = event.query, isLoading = event.query.isNotEmpty())
            }
        }
    }
}
package com.clownteam.collection_datasource.mappers

import com.clownteam.collection_domain.CollectionSortOption
import com.clownteam.collection_domain.CollectionSortType
import com.clownteam.collection_domain.SortDirection

object CollectionSortOptionApiMapper {

    private const val TITLE_SORT_STRING = "-title"
    private const val RATING_SORT_STRING = "rating"
    private const val DATE_SORT_STRING = "date_create"

    fun map(sortOption: CollectionSortOption): String {
        var optionString = when (sortOption.type) {
            CollectionSortType.Rating -> RATING_SORT_STRING
            CollectionSortType.Title -> TITLE_SORT_STRING
            CollectionSortType.Date -> DATE_SORT_STRING
        }

        if (sortOption.direction == SortDirection.DESC) {
            optionString = if (optionString.contains("-")) {
                optionString.replace("-", "")
            } else {
                "-$optionString"
            }
        }

        return optionString
    }
}
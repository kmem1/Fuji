package com.clownteam.search_domain

sealed class SearchFilter {
//    object All: SearchFilter()
    object Courses: SearchFilter()
    object Collections: SearchFilter()
    object Authors: SearchFilter()
}
package com.example.ui_user_archive

sealed class ArchiveScreenEvent {

    class SetQuery(val query: String) : ArchiveScreenEvent()
}
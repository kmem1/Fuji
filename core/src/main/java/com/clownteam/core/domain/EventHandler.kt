package com.clownteam.core.domain

interface EventHandler<T> {
    fun obtainEvent(event: T)
}
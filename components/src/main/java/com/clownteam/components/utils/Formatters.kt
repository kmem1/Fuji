package com.clownteam.components.utils

fun getMembersCountString(count: Int) =
    if (count > 10000) {
        "${count / 1000}K"
    } else {
        "$count"
    }
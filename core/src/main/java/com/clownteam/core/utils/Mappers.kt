package com.clownteam.core.utils

interface ImageUrlMapper {
    fun mapImageUrl(baseUrl: String, relativeImgUrl: String): String
}

class ImageUrlMapperImpl(): ImageUrlMapper {

    override fun mapImageUrl(baseUrl: String, relativeImgUrl: String): String {
        var imgUrl =
            if (baseUrl.last() == '/') baseUrl.substring(0 until baseUrl.lastIndex) else baseUrl
        imgUrl += relativeImgUrl

        return imgUrl
    }
}
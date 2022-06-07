package com.clownteam.fuji.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FujiApi {

    const val BASE_URL = "http://dmakger.beget.tech/"
//    private const val API_KEY = "AIzaSyDTiD8EZVID327cQtmcATf4dBY_wRKxGp8"

    private val clientBuilder by lazy {
        val logging =
            HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY }
        OkHttpClient.Builder()
            .addInterceptor(logging)
//            .addInterceptor { chain ->
//                val originalRequest = chain.request()
//
//                val newUrl = originalRequest.url().newBuilder()
//                    .addQueryParameter("key", API_KEY)
//                    .build()
//
//                val newRequest = originalRequest.newBuilder()
//                    .url(newUrl)
//                    .build()
//
//                chain.proceed(newRequest)
//            }
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }

    fun <T> createService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
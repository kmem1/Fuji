package com.clownteam.fuji.di

import android.app.Application
import coil.ImageLoader
import coil.memory.MemoryCache
import com.clownteam.fuji.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoilModule {

    /**
     * Coil docs say: Coil performs best when you create a single ImageLoader and share it throughout your app.
     * This is because each ImageLoader has its own memory cache, bitmap pool, and network observer.
     * For testing: https://coil-kt.github.io/coil/image_loaders/#testing
     */
    @Provides
    @Singleton
    fun provideImageLoader(app: Application): ImageLoader {
        return ImageLoader.Builder(app)
            .error(R.drawable.placeholder_error)
//            .placeholder(R.drawable.placeholder_gray)
            .memoryCache {
                MemoryCache.Builder(app.applicationContext)
                    .maxSizePercent(0.4) // Don't know what is recommended?
                    .build()
            }
            .crossfade(true)
            .build()
    }
}
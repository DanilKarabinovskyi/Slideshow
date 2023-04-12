package danyil.karabinovskyi.slideshow.features.main_screen.di

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import danyil.karabinovskyi.slideshow.features.main_screen.data.SlideshowDataSource
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@Suppress("unused")
@InstallIn(SingletonComponent::class)
object SingletonComponent {

    @Provides
    @Singleton
    internal fun provideSlideshowDataSource(): SlideshowDataSource {
        return SlideshowDataSource.Base()
    }

}

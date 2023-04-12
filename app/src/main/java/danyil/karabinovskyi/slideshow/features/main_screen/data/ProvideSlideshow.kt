package danyil.karabinovskyi.slideshow.features.main_screen.data

import com.google.gson.GsonBuilder
import danyil.karabinovskyi.slideshow.features.main_screen.data.models.Slideshow
import danyil.karabinovskyi.slideshow.features.main_screen.data.models.SlideshowInfo
import javax.inject.Inject

interface SlideshowDataSource {
    fun getSlideshowInfo(): Slideshow

    class Base @Inject constructor() : SlideshowDataSource {
        override fun getSlideshowInfo(): Slideshow {
            val gson = GsonBuilder().create()
            return gson.fromJson(SlideshowInfo.info, Slideshow::class.java)
        }

    }
}
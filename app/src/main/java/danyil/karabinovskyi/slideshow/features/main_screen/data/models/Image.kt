package danyil.karabinovskyi.slideshow.features.main_screen.data.models

data class Image(
    val assetId: Int,
    val assetTitle: String,
    val flipHorizontal: Boolean,
    val flipVertical: Boolean,
    val url: String
)
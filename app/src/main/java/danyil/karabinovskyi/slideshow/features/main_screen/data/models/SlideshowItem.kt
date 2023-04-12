package danyil.karabinovskyi.slideshow.features.main_screen.data.models

data class SlideshowItem(
    val id: Int,
    val image: Image,
    val order: Int,
    val sentences: List<Sentence>,
    val template: String
)
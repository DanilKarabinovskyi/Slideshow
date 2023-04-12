package danyil.karabinovskyi.slideshow.features.main_screen.presentation

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import danyil.karabinovskyi.slideshow.features.main_screen.data.SlideshowDataSource
import danyil.karabinovskyi.slideshow.features.main_screen.data.models.Sentence
import danyil.karabinovskyi.slideshow.features.main_screen.data.models.Slideshow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val slideshowDataSource: SlideshowDataSource
) : ViewModel() {

    val isChecked = mutableStateOf(false)
    private val _state = mutableStateOf(ScreenState())
    val state: State<ScreenState> = _state

    init {
        getSlideShow()
    }

    private fun getSlideShow() {
        val result = slideshowDataSource.getSlideshowInfo()
        val wordsForChecking = result.last().sentences.map { sentence: Sentence ->
            ListItemModel(
                text = mutableStateOf(
                    sentence.answer
                )
            )
        }
        result.apply {
            _state.value = ScreenState(
                initialWordsForSelecting = wordsForChecking.reversed().toMutableList()
                    .toMutableStateList(),
                initialWordsForShowing = last().sentences.map { sentence: Sentence -> sentence.sentence }
                    .toMutableList(),
                wordsForChecking = wordsForChecking.toMutableStateList(),
                image = first().image.url
            )
        }
    }

    fun moveItem(from: Int, to: Int) {
        _state.value.initialWordsForSelecting = _state.value.initialWordsForSelecting.apply {
            add(to, removeAt(from))
        }
    }

    fun checkResult() {
        viewModelScope.launch(Dispatchers.Default) {
            val wordsForChecking = mutableStateListOf<ListItemModel>()
            val wordsForSelection = mutableStateListOf<ListItemModel>()
            state.value.wordsForChecking.forEachIndexed { index, item ->
                if (item.text.value != state.value.initialWordsForSelecting[index].text.value) {
                    wordsForChecking.add(
                        state.value.wordsForChecking[index].copy(
                            statesOfPhrase = mutableStateOf(
                                StatesOfPhrase.WARNING
                            )
                        )
                    )
                    wordsForSelection.add(
                        state.value.initialWordsForSelecting[index].copy(
                            statesOfPhrase = mutableStateOf(StatesOfPhrase.WRONG)
                        )
                    )
                } else {
                    wordsForChecking.add(
                        state.value.wordsForChecking[index].copy(
                            statesOfPhrase = mutableStateOf(
                                StatesOfPhrase.RIGHT
                            )
                        )
                    )
                    wordsForSelection.add(
                        state.value.initialWordsForSelecting[index].copy(
                            statesOfPhrase = mutableStateOf(StatesOfPhrase.NONE)
                        )
                    )
                }
            }
            repeat(state.value.wordsForChecking.size) {
                state.value.wordsForChecking.removeAt(it)
                state.value.wordsForChecking.add(it, wordsForChecking[it])
                state.value.initialWordsForSelecting.removeAt(it)
                state.value.initialWordsForSelecting.add(it, wordsForSelection[it])
            }
            isChecked.value = true
        }
    }
}

class ScreenState(
    val initialWordsForShowing: List<String> = mutableListOf<String>(),
    var initialWordsForSelecting: SnapshotStateList<ListItemModel> = mutableStateListOf<ListItemModel>(),
    val wordsForChecking: SnapshotStateList<ListItemModel> = mutableStateListOf<ListItemModel>(),
    val image: String = "",
)

data class ListItemModel(
    val text: MutableState<String> = mutableStateOf(""),
    val statesOfPhrase: MutableState<StatesOfPhrase> = mutableStateOf(StatesOfPhrase.NONE)
)

enum class StatesOfPhrase {
    NONE, WRONG, WARNING, RIGHT
}
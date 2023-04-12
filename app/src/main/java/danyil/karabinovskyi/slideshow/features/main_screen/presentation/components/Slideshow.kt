package danyil.karabinovskyi.slideshow.features.main_screen.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import danyil.karabinovskyi.slideshow.features.main_screen.data.models.Slideshow
import danyil.karabinovskyi.slideshow.features.main_screen.presentation.MainScreenViewModel
import danyil.karabinovskyi.slideshow.features.main_screen.presentation.ScreenState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Slideshow(
    screenState: State<ScreenState>,
    viewModel: MainScreenViewModel,
    onNavigateUp: () -> Unit
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pagerState,
            pageCount = 2
        ) { page ->
            when (page) {
                0 -> {
                    ImageScreen(img = screenState.value.image, onBottomButtonClick = {
                        scope.launch {
                            pagerState.scrollToPage(1)
                        }
                    }, onNavigateUp = {
                        onNavigateUp()
                    })
                }
                1 -> {
                    QuizScreen(screenState, viewModel = viewModel,onNavigateUp = {
                        scope.launch {
                            pagerState.scrollToPage(0)
                        }
                    })
                }
            }
        }
    }

}
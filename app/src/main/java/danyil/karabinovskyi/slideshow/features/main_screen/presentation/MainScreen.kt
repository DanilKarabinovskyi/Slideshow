package danyil.karabinovskyi.slideshow.features.main_screen.presentation

import android.view.View
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import danyil.karabinovskyi.slideshow.features.main_screen.presentation.components.Slideshow

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel(), onNavigateUp: () -> Unit) {

    val state = viewModel.state

    Slideshow(state, viewModel = viewModel) {
        onNavigateUp()
    }
}
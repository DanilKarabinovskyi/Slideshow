package danyil.karabinovskyi.slideshow.features.main_screen.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import danyil.karabinovskyi.slideshow.core.components.drag_and_drop.rememberDragDropListState
import danyil.karabinovskyi.slideshow.core.components.text.DefaultText
import danyil.karabinovskyi.slideshow.features.main_screen.presentation.ListItemModel
import danyil.karabinovskyi.slideshow.features.main_screen.presentation.MainScreenViewModel
import danyil.karabinovskyi.slideshow.features.main_screen.presentation.ScreenState
import danyil.karabinovskyi.slideshow.features.main_screen.presentation.StatesOfPhrase
import danyil.karabinovskyi.slideshow.ui.theme.Blue

@Composable
fun QuizScreen(
    screenState: State<ScreenState>,
    viewModel: MainScreenViewModel,
    onNavigateUp: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        VerticalWordsList(
            modifier = Modifier.weight(6f),
            listForShowing = screenState.value.initialWordsForShowing,
            listForChecking = screenState.value.wordsForChecking,
            viewModel.isChecked,
        )
        VerticalReorderList(
            modifier = Modifier.weight(3f),
            list = screenState.value.initialWordsForSelecting,
            viewModel = viewModel
        )
        ColumnWithButtons(
            modifier = Modifier.weight(1f),
            onCancelClick = { onNavigateUp() },
            onBottomButtonClick = {
                viewModel.checkResult()
            },
            isChecked = viewModel.isChecked,
            isQuizScreen = true
        )
    }


}

@Composable
private fun VerticalWordsList(
    modifier: Modifier = Modifier,
    listForShowing: List<String>,
    listForChecking: SnapshotStateList<ListItemModel>,
    isChecked: State<Boolean>
) {
    val elementWidth = (LocalConfiguration.current.screenWidthDp / 5).dp
    val lazyListState = rememberLazyListState()
    LazyColumn(
        modifier = modifier.fillMaxHeight(),
        state = lazyListState,
    ) {
        items(listForShowing.size) { index ->
            Row(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DefaultText(
                    text = listForShowing[index],
                    modifier = Modifier
                        .padding(30.dp)
                        .width(elementWidth)
                )
                Spacer(modifier = Modifier.weight(2f))
                CheckableItem(
                    item = listForChecking[index],
                    isWordForSelection = false,
                    isChecked = isChecked
                )
                Spacer(modifier = Modifier.weight(2f))
            }
        }
    }
}

@Composable
private fun VerticalReorderList(
    modifier: Modifier = Modifier,
    list: SnapshotStateList<ListItemModel>,
    viewModel: MainScreenViewModel,
) {
    val lazyListState = rememberLazyListState()
    val reorderState =
        rememberDragDropListState(lazyListState = lazyListState, onMove = { from, to ->
            viewModel.moveItem(from, to)
        })
    val finalModifier = if (!viewModel.isChecked.value) modifier
        .pointerInput(viewModel.isChecked.value) {
            detectDragGesturesAfterLongPress(
                onDrag = { change, offset ->
                    change.consume()
                    reorderState.onDrag(offset)
                },
                onDragStart = { offset ->
                    reorderState.onDragStart(offset)
                },
                onDragEnd = {
                    reorderState.onDragInterrupted()
                },
                onDragCancel = {
                    reorderState.onDragInterrupted()
                }
            )
        } else modifier
    LazyColumn(
        modifier = finalModifier
            .fillMaxHeight(),
        state = lazyListState
    ) {
        items(list.size) { index ->
            Row {
                Spacer(modifier = Modifier.weight(1f))
                CheckableItem(item = list[index], modifier = Modifier
                    .graphicsLayer(translationY = reorderState.elementDisplacement
                        .takeIf { index == reorderState.currentIndexOfDraggedItem } ?: 0f),
                    isWordForSelection = true, isChecked = viewModel.isChecked)
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}

@Composable
fun CheckableItem(
    modifier: Modifier = Modifier,
    item: ListItemModel,
    isWordForSelection: Boolean,
    isChecked: State<Boolean>
) {
    val borderColor = when (item.statesOfPhrase.value) {
        StatesOfPhrase.NONE -> {
            Color.LightGray
        }
        StatesOfPhrase.RIGHT -> {
            Color.Green
        }
        StatesOfPhrase.WARNING -> {
            Color.Yellow
        }
        StatesOfPhrase.WRONG -> {
            Color.Red
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height(120.dp)
            .padding(end = 20.dp)
    ) {
        when (item.statesOfPhrase.value) {
            StatesOfPhrase.NONE -> {
                Spacer(modifier = Modifier.height(24.dp))
            }
            StatesOfPhrase.RIGHT -> {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color.Green
                )
            }
            StatesOfPhrase.WARNING -> {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    tint = Color.Yellow
                )
            }
            StatesOfPhrase.WRONG -> {
                Icon(imageVector = Icons.Filled.Close, contentDescription = null, tint = Color.Red)
            }
        }
        Box(
            modifier = modifier.border(
                2.dp, shape = RectangleShape, color = if(isWordForSelection && item.statesOfPhrase.value == StatesOfPhrase.NONE && !isChecked.value) Blue else borderColor
            )
        ) {
            val text =
                if ((isWordForSelection &&
                            ((item.statesOfPhrase.value != StatesOfPhrase.NONE && isChecked.value) ||
                                    (item.statesOfPhrase.value == StatesOfPhrase.NONE && !isChecked.value))) ||
                    ((!isWordForSelection) && isChecked.value)
                ) {
                    item.text.value
                } else {
                    "                            "
                }
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 25.dp, vertical = 15.dp),
                color = if(isWordForSelection && item.statesOfPhrase.value == StatesOfPhrase.NONE) Blue else Color.Black
            )
        }
    }

}
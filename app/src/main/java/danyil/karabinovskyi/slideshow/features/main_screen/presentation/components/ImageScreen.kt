package danyil.karabinovskyi.slideshow.features.main_screen.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import danyil.karabinovskyi.slideshow.ui.theme.Blue

@Composable
fun ImageScreen(img: String, onBottomButtonClick: () -> Unit, onNavigateUp: () -> Unit) {
    Row(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.weight(0.9f),
            model = img,
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
        ColumnWithButtons(
            onCancelClick = { onNavigateUp() },
            onBottomButtonClick = { onBottomButtonClick() })
    }
}

@Composable
fun ColumnWithButtons(
    modifier: Modifier = Modifier,
    onCancelClick: () -> Unit,
    onBottomButtonClick: () -> Unit,
    isQuizScreen:Boolean = false,
    isChecked: State<Boolean> = mutableStateOf(false),
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .padding(15.dp)
                .size(24.dp)
                .clickable {
                    onCancelClick()
                },
            imageVector = Icons.Default.Close,
            tint = Blue,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            modifier = Modifier.padding(15.dp),
            onClick = { onBottomButtonClick() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Blue
            ),
            enabled = !isChecked.value
        ) {
            Icon(
                modifier = Modifier
                    .padding(2.dp)
                    .size(24.dp),
                imageVector = if(isQuizScreen && isChecked.value || !isQuizScreen)Icons.Default.KeyboardArrowRight else Icons.Default.Check,
                tint = Color.White,
                contentDescription = null,
            )
        }
    }
}
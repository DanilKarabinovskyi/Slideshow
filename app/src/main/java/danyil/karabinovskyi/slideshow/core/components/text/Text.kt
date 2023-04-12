package danyil.karabinovskyi.slideshow.core.components.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun DefaultText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = 20.sp,
    maxLines:Int = 5,
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontWeight = fontWeight,
        fontSize = fontSize,
        maxLines = maxLines,
    )
}
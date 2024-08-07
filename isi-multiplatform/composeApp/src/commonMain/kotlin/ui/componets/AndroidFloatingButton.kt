package ui.componets

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import getPlatform
import ui.theme.getColorsTheme

@Composable
fun AndroidFloatingButton(isExpanded: MutableState<Boolean>, modifier: Modifier) {
    val colors = getColorsTheme()
    if (getPlatform().name.startsWith("Android")) {
        FloatingActionButton(
            onClick = { isExpanded.value = !isExpanded.value },
            modifier = modifier,
            backgroundColor = colors.BackgroundColor,
            contentColor = colors.TextColor
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Toggle sidebar")
        }
    }
}
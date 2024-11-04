package ui.componets

import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import getPlatform

@Composable
fun AndroidFloatingButton(isExpanded: MutableState<Boolean>, modifier: Modifier) {
    if (getPlatform().name.startsWith("Android")) {
        FloatingActionButton(
            onClick = { isExpanded.value = !isExpanded.value },
            modifier = modifier
        ) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Toggle sidebar")
        }
    }
}
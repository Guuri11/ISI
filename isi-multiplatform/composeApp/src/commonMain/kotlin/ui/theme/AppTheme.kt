package ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppTheme(content : @Composable () -> Unit ) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(primary = Color.Black),
        shapes = MaterialTheme.shapes.copy(
            small = AbsoluteCutCornerShape(0.dp),
            medium = AbsoluteCutCornerShape(0.dp),
            large = AbsoluteCutCornerShape(0.dp)
        )
    )
    {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}

@Composable
fun getColorsTheme(): DarkModeColors {
    val isDarkMode = true

    val Purple = Color(0xFF6A66FF)
    val ColorExpenseItem = if (isDarkMode) Color(0xFF090808) else Color(0xFFF1F1F1)
    val BackgroundColor = if (isDarkMode) Color(0xFF212121) else Color.White
    val TextColor = if (isDarkMode) Color(0xFFECECEC) else Color.Black
    val AddIconColor = if (isDarkMode) Purple else Color.Black
    val ColorArrowRound = if (isDarkMode) Purple else Color.Gray.copy(alpha = .2f)

    return DarkModeColors(
        Purple, ColorExpenseItem, BackgroundColor, TextColor, AddIconColor, ColorArrowRound
    )
}

data class DarkModeColors(
    val Purple: Color,
    val ColorExpenseItem: Color,
    val BackgroundColor: Color,
    val TextColor: Color,
    val AddIconColor: Color,
    val ColorArrowRound: Color
)
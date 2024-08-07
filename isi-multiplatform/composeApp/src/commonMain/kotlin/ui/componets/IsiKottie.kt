package ui.componets

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import isi_multiplatform.composeapp.generated.resources.Res
import kottieAnimation.KottieAnimation
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import utils.KottieConstants

@OptIn(ExperimentalResourceApi::class)
@Composable
fun IsiKottie(size: Dp) {
    var animation by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        animation = Res.readBytes("files/robot.json").decodeToString()
    }

    val composition = rememberKottieComposition(
        spec = KottieCompositionSpec.File(animation)
    )

    val animationState by animateKottieCompositionAsState(
        composition = composition,
        iterations = KottieConstants.IterateForever
    )

    KottieAnimation(
        composition = composition,
        progress = { animationState.progress },
        modifier = Modifier
            .size(size)
    )

}
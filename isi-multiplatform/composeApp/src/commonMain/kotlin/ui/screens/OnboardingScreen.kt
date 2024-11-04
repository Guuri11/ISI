package ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.componets.IsiKottie

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
) {
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState, modifier = Modifier.fillMaxSize()
    ) { page ->
        PageContent(page = page, onFinish = onFinish, onNextPage = {
            coroutineScope.launch {
                pagerState.animateScrollToPage(page + 1)
            }
        })
    }
}

@Composable
fun PageContent(
    page: Int,
    onFinish: () -> Unit,
    onNextPage: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (page) {
            0 -> {
                OnboardingPageOne(onNextPage)
            }

            1 -> {
                OnboardingPageTwo(onNextPage)
            }

            else -> {
                OnboardingPageThree(onFinish)
            }
        }
    }
}

@Composable
fun OnboardingPageOne(
    onNextPage: () -> Unit,
) {
    IsiKottie(150.dp)
    Text("Hi there! I am ISI", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)

    Spacer(modifier = Modifier.height(16.dp))

    Text("I’m here to help you simplify your day-to-day tasks. Whether you have questions, need reminders, or just want to chat, I’m ready to assist you!", textAlign = TextAlign.Center)

    Spacer(modifier = Modifier.height(32.dp))

    Button(onClick = onNextPage) {
        Text("Swipe right to see what I can do for you!")
    }
}

@Composable
fun OnboardingPageTwo(
    onNextPage: () -> Unit,
) {
    IsiKottie(150.dp)
    Text("Let’s Chat Naturally!", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)

    Spacer(modifier = Modifier.height(16.dp))

    Text("You can talk to me just like you would with a friend. Ask me questions, request information, or just have a casual conversation. I’m designed to understand you and provide accurate, relevant answers.", textAlign = TextAlign.Center)

    Spacer(modifier = Modifier.height(32.dp))

    Button(onClick = onNextPage) {
        Text("Swipe to discover more of my useful features!")
    }
}

@Composable
fun OnboardingPageThree(
    onFinish: () -> Unit,
) {
    IsiKottie(150.dp)
    Text("Explore My Extra Features!", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)

    Spacer(modifier = Modifier.height(16.dp))

    Text("Ready to start your adventure with me? Tap the start button, and let’s make your day easier together!", textAlign = TextAlign.Center)

    Spacer(modifier = Modifier.height(32.dp))

    Button(onClick = onFinish) {
        Text("Start")
    }
}

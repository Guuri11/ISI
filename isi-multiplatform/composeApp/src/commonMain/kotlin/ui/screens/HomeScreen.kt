package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import ui.componets.IsiKottie

@Composable
fun HomeScreen(goTo: (String) -> Unit) {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val chatListState = rememberLazyListState()

    LaunchedEffect(uiState.commands) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo centered
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            IsiKottie(size = if (uiState.commands.isEmpty()) 300.dp else 100.dp)
        }

        Text("Hello there, Sergio!", style = MaterialTheme.typography.titleLarge)
        Text("How can I assist you?", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Direct access section with grid
        Text(
            "Your direct access",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Bookmarks section with grid
        Text(
            "Bookmarks",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
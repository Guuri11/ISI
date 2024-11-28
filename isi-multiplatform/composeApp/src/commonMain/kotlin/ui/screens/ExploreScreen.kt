package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import navigator.Screen
import ui.componets.IsiKottie

@Composable
fun ExploreScreen(goTo: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            IsiKottie(size = 300.dp)
        }
        Text("Hello there, Sergio!", style = MaterialTheme.typography.titleLarge)
        Text("How can I assist you?", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // Explore section with grid
        Text(
            "Explore",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Cards
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columns
            modifier = Modifier.fillMaxWidth()
        ) {
            items(listOf(Screen.CarCoords)) {
                ExploreCard(it, goTo)
            }
        }
    }
}

@Composable
private fun ExploreCard(it: Screen.CarCoords, goTo: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .size(155.dp)
            .clickable {
                goTo(it.route)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = it.icon,
                contentDescription = it.label,
                modifier = Modifier.size(40.dp)
            )
            Text(
                it.label,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
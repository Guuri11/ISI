package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.jordond.compass.Location
import dev.jordond.compass.Place
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.TrackingStatus
import dev.jordond.compass.geolocation.isPermissionDeniedForever
import getGeoLocator
import getGeocoding
import getPlatform
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import openMaps
import presentation.LocalIsiViewModel
import ui.componets.IsiKottie

@Composable
fun CarCoordsScreen() {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var location by remember { mutableStateOf<Location?>(null) }
    var street by remember { mutableStateOf<Place?>(null) }

    val geolocator = getGeoLocator()

    LaunchedEffect(Unit) {
        if (getPlatform().name.startsWith("Android")) {
            try {
                geolocator.startTracking()

                geolocator.trackingStatus.collect { trackingStatus ->
                    when (trackingStatus) {
                        is TrackingStatus.Update -> {
                            location = trackingStatus.location
                            street = getGeocoding().placeOrNull(trackingStatus.location.coordinates)
                        }

                        is TrackingStatus.Error -> {
                            val error: GeolocatorResult.Error = trackingStatus.cause
                            if (error.isPermissionDeniedForever()) {
                                // TODO: handle error
                                println("Permission denied forever")
                            }
                        }

                        else -> {}
                    }
                }
            } finally {
                // Para el tracking al salir del composable
                geolocator.stopTracking()
            }
        }
    }

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

        Text(
            "${location?.coordinates?.latitude};${location?.coordinates?.longitude}",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                viewModel.onSettingsChange(
                    uiState.settings.copy(
                        carLatitude = location?.coordinates?.latitude,
                        carLongitude = location?.coordinates?.longitude,
                        carStreet = street?.street
                    )
                )
            },
        ) {
            Text("Save car coordinates")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (getPlatform().name.startsWith("Android")) {
            if (uiState.settings.carStreet != null) {
                Text("You car is in", style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                Text(
                    uiState.settings.carStreet!!,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                StreetCard(uiState.settings.carLatitude!!, uiState.settings.carLongitude!!)
            }
        } else {
            Text("This feature is only available in mobile for now", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun StreetCard(latitude: Double, longitude: Double) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .size(155.dp)
            .clickable {
                openMaps(latitude, longitude)
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
                imageVector = Icons.Default.Explore,
                contentDescription = "Street",
                modifier = Modifier.size(40.dp)
            )
            Text(
                "Open in google maps",
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
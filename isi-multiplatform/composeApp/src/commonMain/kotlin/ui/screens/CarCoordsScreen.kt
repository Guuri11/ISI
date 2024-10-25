package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jordond.compass.Location
import dev.jordond.compass.Place
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.currentLocationOrNull
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import getGeoLocator
import getGeocoding
import openMaps
import getPlatform

@Composable
fun CarCoordsScreen(goTo: (String) -> Unit) {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var location by remember { mutableStateOf<Location?>(null) }
    var street by remember { mutableStateOf<Place?>(null) }

    LaunchedEffect(Unit) {
        if (getPlatform().name.startsWith("Android")) {
            location = getGeoLocator().currentLocationOrNull()
            if (location != null) {
                street = getGeocoding().placeOrNull(location!!.coordinates)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row {
            Column(
                modifier = Modifier.weight(3f).fillMaxHeight().padding(16.dp)
            ) {
                if (uiState.enviroment != null) {
                    IconButton(onClick = {
                        goTo("/home")
                    }) {
                        Icon(
                            modifier = Modifier.padding(start = 16.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (getPlatform().name.startsWith("Android")) {
                            Text(
                                text = "Car Coordinates",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = location?.coordinates?.latitude.toString() + ", " + location?.coordinates?.longitude.toString(),
                            )
                            if (street?.street !== null) {
                                Text(
                                    text = street?.street!!,
                                )
                                Button(
                                    onClick = {
                                        openMaps(location?.coordinates!!.latitude, location?.coordinates?.longitude!!)
                                    },
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text(
                                        text = "Open in Google Maps",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Car Coordinates",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "Feature only available on Android",
                            )
                        }
                    }
                }
            }
        }
    }
}
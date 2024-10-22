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
import ui.theme.getColorsTheme
import getGeoLocator
import getGeocoding
import openMaps
import getPlatform

@Composable
fun CarCoordsScreen(goTo: (String) -> Unit) {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = getColorsTheme()
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
                modifier = Modifier.background(colors.BackgroundColor).weight(3f).fillMaxHeight().padding(16.dp)
            ) {
                if (uiState.enviroment != null) {
                    IconButton(onClick = {
                        goTo("/home")
                    }) {
                        Icon(
                            modifier = Modifier.padding(start = 16.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = colors.TextColor,
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
                                color = colors.TextColor
                            )
                            Text(
                                text = location?.coordinates?.latitude.toString() + ", " + location?.coordinates?.longitude.toString(),
                                color = colors.TextColor
                            )
                            if (street?.street !== null) {
                                Text(
                                    text = street?.street!!,
                                    color = colors.TextColor
                                )
                                Button(
                                    onClick = {
                                        openMaps(location?.coordinates!!.latitude, location?.coordinates?.longitude!!)
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = colors.Purple),
                                    modifier = Modifier.padding(top = 16.dp)
                                ) {
                                    Text(
                                        text = "Open in Google Maps",
                                        color = colors.TextColor,
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
                                color = colors.TextColor
                            )
                            Text(
                                text = "Feature only available on Android",
                                color = colors.TextColor
                            )
                        }
                    }
                }
            }
        }
    }
}
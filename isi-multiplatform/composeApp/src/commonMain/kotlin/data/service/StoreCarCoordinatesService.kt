package data.service

import dev.jordond.compass.Location
import dev.jordond.compass.Place
import dev.jordond.compass.geocoder.placeOrNull
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.TrackingStatus
import dev.jordond.compass.geolocation.isPermissionDeniedForever
import getGeocoding
import getPlatform
import kotlinx.coroutines.flow.take
import presentation.IsiViewModel

suspend fun storeCarCoordinatesService(
    viewModel: IsiViewModel,
    updateLocation: (Location?) -> Unit,
    updateStreet: (Place?) -> Unit
) {
    if (getPlatform().name.startsWith("Android")) {
        try {
            viewModel.uiState.value.geolocator.startTracking()

            viewModel.uiState.value.geolocator.trackingStatus.take(3).collect { trackingStatus ->
                when (trackingStatus) {
                    is TrackingStatus.Update -> {
                        updateLocation(trackingStatus.location)
                        updateStreet(getGeocoding().placeOrNull(trackingStatus.location.coordinates))
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
            viewModel.uiState.value.geolocator.stopTracking()
        }
    }
}
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geolocation.Geolocator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun getGeoLocator(): Geolocator

expect fun getGeocoding(): Geocoder

expect fun openMaps(latitude: Double, longitude: Double)

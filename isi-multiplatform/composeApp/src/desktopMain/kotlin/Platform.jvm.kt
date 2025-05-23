import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geolocation.Geolocator
import org.isi.domain.models.AppsAvailable

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getGeoLocator(): Geolocator {
    throw Exception("Geocoder not available for Desktop")
}

actual fun getGeocoding(): Geocoder {
    throw Exception("Geocoding not available for Desktop")
}


actual fun openMaps(latitude: Double, longitude: Double) {
    throw Exception("Maps not available for Desktop")
}

actual fun openApp(app: AppsAvailable, args: String) {
    throw Exception("Open app not available for Desktop")
}
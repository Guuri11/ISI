import android.os.Build
import dev.jordond.compass.geocoder.Geocoder
import dev.jordond.compass.geocoder.mobile
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.mobile
import utils.AppContext
import utils.openGoogleMapsWithCoordinates

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun getGeoLocator(): Geolocator {
    return Geolocator.mobile()
}

actual fun getGeocoding(): Geocoder {
    return Geocoder.mobile()
}

actual fun openMaps(latitude: Double, longitude: Double) {
    openGoogleMapsWithCoordinates(context = AppContext, latitude, longitude)
}
package utils

import android.content.Intent
import android.net.Uri
import platform.AppContext

fun openGoogleMapsWithCoordinates(context: AppContext, latitude: Double, longitude: Double) {

    val uri = Uri.parse("https://maps.google.com/maps/search/$latitude,$longitude")
    val intent = Intent(Intent.ACTION_VIEW, uri)

    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    context.get().startActivity(intent)
}

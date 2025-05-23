package utils

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import org.isi.domain.models.AppsAvailable
import platform.AppContext

fun openApp(context: AppContext, app: AppsAvailable, args: String? = null) {

    when (app) {
        AppsAvailable.CAMERA_VISION -> openCameraVision(context)
        AppsAvailable.YOUTUBE -> openYoutube(context, args.orEmpty())
        AppsAvailable.SPOTIFY -> openSpotify(context, args.orEmpty())
        else -> {
            Toast.makeText(context.get(), "Aplicación no encontrada", Toast.LENGTH_LONG).show()
        }
    }
}

private fun openCameraVision(context: AppContext) {
    try {
        val intent = Intent().apply {
            component = ComponentName(
                "com.google.ar.lens",
                "com.google.vr.apps.ornament.app.lens.LensLauncherActivity"
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.get().startActivity(intent)
    } catch (e: Exception) {
        Log.e("Lens", "Error opening Google Lens: ${e.message}")
        showToast(context, "Google Lens no está instalado en este dispositivo")
    }
}

private fun openYoutube(context: AppContext, query: String?) {
    val url = "https://www.youtube.com${query?.let { "/results?search_query=$it" } ?: ""}"
    openUrl(context, url, "No se pudo abrir YouTube")
}

private fun openSpotify(context: AppContext, query: String?) {
    val url = "https://open.spotify.com${query?.let { "/search/$it" } ?: ""}"
    openUrl(context, url, "Spotify no está instalado en este dispositivo")
}

private fun openUrl(context: AppContext, url: String, errorMessage: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.get().startActivity(intent)
    } catch (e: Exception) {
        Log.e("AppLauncher", "Error opening URL: ${e.message}")
        showToast(context, errorMessage)
    }
}

private fun showToast(context: AppContext, message: String) {
    Toast.makeText(context.get(), message, Toast.LENGTH_LONG).show()
}
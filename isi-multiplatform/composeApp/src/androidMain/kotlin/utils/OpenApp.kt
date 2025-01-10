package utils

import android.content.ComponentName
import android.content.Intent
import android.util.Log
import android.widget.Toast
import org.isi.domain.models.AppsAvailable
import platform.AppContext

fun openApp(context: AppContext, app: AppsAvailable) {

    when(app) {
        AppsAvailable.CAMERA_VISION -> openCameraVision(context)

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
        Log.e("Lens", "Error lens: ${e.message}")
        Toast.makeText(context.get(), "Google Lens no está instalado en este dispositivo", Toast.LENGTH_LONG).show()
    }
}
package com.guuri11.isi_wear.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.annotation.RequiresApi


class AlarmReceiver : BroadcastReceiver() {
    private var mediaPlayer: MediaPlayer? = null
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent) {
        // TODO: change Alarma! string for user request
        Toast.makeText(context, "Alarma!", Toast.LENGTH_LONG).show()
        var alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmTone == null) {
            alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        mediaPlayer = MediaPlayer.create(context, alarmTone)
        if (mediaPlayer != null) {
            mediaPlayer!!.start()
        }
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
    }
}

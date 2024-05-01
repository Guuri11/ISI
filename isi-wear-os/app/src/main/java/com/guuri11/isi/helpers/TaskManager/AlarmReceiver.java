package com.guuri11.isi.helpers.TaskManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: change Alarma! string for user request
        Toast.makeText(context, "Alarma!", Toast.LENGTH_LONG).show();

        Uri alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmTone == null) {
            alarmTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        mediaPlayer = MediaPlayer.create(context, alarmTone);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AlarmReceiver that = (AlarmReceiver) obj;
        return mediaPlayer.equals(that.mediaPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediaPlayer);
    }

    @Override
    protected void finalize() throws Throwable {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.finalize();
    }
}

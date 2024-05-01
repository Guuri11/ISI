package com.guuri11.isi.helpers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.guuri11.isi.helpers.TaskManager.AlarmReceiver;

public class AlarmHelper {
    private final Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerTime = System.currentTimeMillis() + 10 * 1000;
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerTime, pendingIntent);
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
    }
}


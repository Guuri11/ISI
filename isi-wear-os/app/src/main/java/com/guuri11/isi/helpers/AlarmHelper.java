package com.guuri11.isi.helpers;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.guuri11.isi.helpers.TaskManager.AlarmReceiver;
import com.guuri11.isi.persistance.ErrorMessage;
import com.guuri11.isi.persistance.WordsToNumber;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlarmHelper {
    private final Context context;

    public AlarmHelper(Context context) {
        this.context = context;
    }

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarm(long triggerAtMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerAtMillis, pendingIntent);
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
    }

    public long parseTime(String command) {
        Pattern timePattern = Pattern.compile("(?i)pon\\suna\\salarma\\sen\\s(\\w+)\\s(segundo|minuto|hora)s?");
        Matcher matcher = timePattern.matcher(command);

        if (matcher.find()) {
            String quantityStr = Objects.requireNonNull(matcher.group(1)).toLowerCase();
            String timeUnit = Objects.requireNonNull(matcher.group(2)).toLowerCase();

            int timeAmount = WordsToNumber.numberWords.getOrDefault(quantityStr, -1);
            if (timeAmount == -1) throw new IllegalArgumentException(ErrorMessage.TIME_REQUESTED_UNIT_NOT_RECOGNIZED);

            switch (timeUnit) {
                case "segundo":
                    return System.currentTimeMillis() + timeAmount * 1000L;
                case "minuto":
                    return System.currentTimeMillis() + (long) timeAmount * 60 * 1000;
                case "hora":
                    return System.currentTimeMillis() + (long) timeAmount * 3600 * 1000;
                default:
                    throw new IllegalArgumentException(ErrorMessage.UNIT_TIME_NOT_RECOGNIZED);
            }
        }
        throw new IllegalArgumentException(ErrorMessage.ALARM_COMMAND_COULD_NOT_BE_INTERPRETED);
    }

}


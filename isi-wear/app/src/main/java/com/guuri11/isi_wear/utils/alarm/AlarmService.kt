package com.guuri11.isi_wear.utils.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.guuri11.isi_wear.data.WordToNumber
import com.guuri11.isi_wear.domain.ErrorMessage
import java.util.Locale
import java.util.Objects
import java.util.regex.Pattern

class AlarmService(private val context: Context?) {
    private val wordToNumber = WordToNumber()

    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(triggerAtMillis: Long) {
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmClockInfo = AlarmClockInfo(triggerAtMillis, pendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    fun parseTime(command: String): Long {
        val timePattern =
            Pattern.compile("(?i)pon\\suna\\salarma\\sen\\s(\\w+)\\s(segundo|minuto|hora)s?")
        Log.d("ALARM SERVICE", command)

        val matcher = timePattern.matcher(command)
        if (matcher.find()) {
            val quantityStr =
                Objects.requireNonNull(matcher.group(1)).lowercase(Locale.getDefault())
            val timeUnit = Objects.requireNonNull(matcher.group(2)).lowercase(Locale.getDefault())
            val timeAmount: Int = wordToNumber.getNumber(quantityStr)
            if (timeAmount == -1) throw IllegalArgumentException(ErrorMessage.TIME_REQUESTED_UNIT_NOT_RECOGNIZED)

            return when (timeUnit) {
                "segundo" -> System.currentTimeMillis() + timeAmount * 1000L
                "minuto" -> System.currentTimeMillis() + timeAmount.toLong() * 60 * 1000
                "hora" -> System.currentTimeMillis() + timeAmount.toLong() * 3600 * 1000
                else -> throw IllegalArgumentException(ErrorMessage.UNIT_TIME_NOT_RECOGNIZED)
            }
        }
        throw IllegalArgumentException(ErrorMessage.ALARM_COMMAND_COULD_NOT_BE_INTERPRETED)
    }

}
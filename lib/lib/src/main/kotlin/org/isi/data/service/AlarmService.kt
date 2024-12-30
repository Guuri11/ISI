package org.isi.data.service


import org.isi.data.WordToNumber
import org.isi.domain.models.ErrorMessage
import java.util.Locale
import java.util.Objects
import java.util.regex.Pattern

interface AlarmServiceI {
    fun setAlarm();

    fun parseTime(command: String): Long
}

open class AlarmService: AlarmServiceI {
    private val wordToNumber = WordToNumber()

    override fun setAlarm() {
        throw Exception("TODO implementation")
    }

    override fun parseTime(command: String): Long {
        val timePattern =
            Pattern.compile("(?i)pon\\suna\\salarma\\sen\\s(\\w+)\\s(segundo|minuto|hora)s?")

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
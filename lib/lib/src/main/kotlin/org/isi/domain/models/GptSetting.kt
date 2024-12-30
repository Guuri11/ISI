package org.isi.domain.models


enum class GptSetting(val value: String) {
    GPT_4O_MINI("gpt-4o-mini"),
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_4("gpt-4o");

    companion object {
        fun fromValue(value: String): GptSetting {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("No enum constant for value: $value")
        }
    }
}
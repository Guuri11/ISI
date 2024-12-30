package org.isi.data

class WordToNumber {
    private val numberWords: Map<String, Int> = mapOf(
        "uno" to 1,
        "una" to 1,
        "dos" to 2,
        "tres" to 3,
        "cuatro" to 4,
        "cinco" to 5,
        "seis" to 6,
        "siete" to 7,
        "ocho" to 8,
        "nueve" to 9,
        "diez" to 10,
        "once" to 11,
        "doce" to 12,
        "trece" to 13,
        "catorce" to 14,
        "quince" to 15,
        "veinte" to 20,
        "treinta" to 30
    )

    fun getNumber(word: String): Int {
        return numberWords[word] ?: -1
    }

}
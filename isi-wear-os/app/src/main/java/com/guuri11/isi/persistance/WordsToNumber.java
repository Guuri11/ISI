package com.guuri11.isi.persistance;

import java.util.HashMap;
import java.util.Map;

public class WordsToNumber {
    public static final Map<String, Integer> numberWords = new HashMap<>();
    static {
        numberWords.put("uno", 1);
        numberWords.put("una", 1);
        numberWords.put("dos", 2);
        numberWords.put("tres", 3);
        numberWords.put("cuatro", 4);
        numberWords.put("cinco", 5);
        numberWords.put("seis", 6);
        numberWords.put("siete", 7);
        numberWords.put("ocho", 8);
        numberWords.put("nueve", 9);
        numberWords.put("diez", 10);
        numberWords.put("once", 11);
        numberWords.put("doce", 12);
        numberWords.put("trece", 13);
        numberWords.put("catorce", 14);
        numberWords.put("quince", 15);
        numberWords.put("veinte", 20);
        numberWords.put("treinta", 30);
    }
}

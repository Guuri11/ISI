package com.guuri11.isi.TaskManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Task {
    HELLO(new ArrayList<>(Arrays.asList("buenas", "hola", "buenos días", "buenos dias"))),
    EXIT(new ArrayList<>(Arrays.asList("apágate", "apagate", "cierra el sistema")));

    public final List<String> options;

    private Task(List <String> options) {
        this.options = options;
    }
}

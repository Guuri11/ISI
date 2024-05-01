package com.guuri11.isi.helpers.TaskManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Task {
    ACTIVATE_LOCAL_ASSISTANT(new ArrayList<>(Arrays.asList("activa asistente local", "activa la asistente local", "activa el asistente local", "activa el asistente de local", "local"))),
    ACTIVATE_REMOTE_ASSISTANT(new ArrayList<>(Arrays.asList("activa asistente remoto", "activa la asistente remoto", "activa el asistente remoto", "activa el asistente de remoto", "remoto"))),
    CREATE_ALARM(new ArrayList<>(Arrays.asList("con una alarma en", "pon una alarma en"))),
    EXIT(new ArrayList<>(Arrays.asList("apágate", "apagate", "cierra el sistema")));

    public final List<String> options;

    Task(List<String> options) {
        this.options = options;
    }
}

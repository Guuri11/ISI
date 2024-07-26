package com.guuri11.isi_wear.domain
enum class Task(val options: List<String>) {
    ACTIVATE_LOCAL_ASSISTANT(listOf(
        "activa asistente local",
        "activa la asistente local",
        "activa el asistente local",
        "activa el asistente de local",
        "local"
    )),
    ACTIVATE_REMOTE_ASSISTANT(listOf(
        "activa asistente remoto",
        "activa la asistente remoto",
        "activa el asistente remoto",
        "activa el asistente de remoto",
        "remoto"
    )),
    CREATE_ALARM(listOf(
        "con una alarma en",
        "pon una alarma en"
    )),
    EXIT(listOf(
        "apágate",
        "apagate",
        "cierra el sistema"
    ))
}

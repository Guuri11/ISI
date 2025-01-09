package org.isi.domain.models

// TODO: handle onlyLocal on apps
/**
 * @param onlyLocal indicates that this task will be executed only in the current device.
 * @param value name to show in the app
 * @param show: boolean to indicate if we show this type of tasks to the user in the UI
 * @param options: If any, the app will match user input with options to run or not a task
 */
enum class TaskType(val onlyLocal: Boolean, val value: String, val show: Boolean, val options: List<String>) {

    /**
     * General purpose conversation with AI assistant
     */
    OTHER_TOPICS(false, "Other", true, listOf()),

    /**
     * Ask for store a resource, this will generate a screenshot
     */
    BOOKMARK_RECOMMENDATIONS(false, "Bookmarks", true, listOf()),

    /**
     * Code assistant
     */
    REFACTOR(false, "Refactor", true, listOf()),

    /**
     * todo: open an app
     */
    OPEN_APP(false, "Open App", false, listOf()),

    /**
     * Reply LinkedIn message to reject an offer
     */
    LINKEDIN_OFFER_REJECTION(false,"Linkedin Offer Rejections", false, listOf()),

    /**
     * todo: ask for current weather
     */
    WEATHER(false,"Weather", false, listOf()),

    /**
     * Change to local assistant
     */
    ACTIVATE_LOCAL_ASSISTANT(true, "Activate local assistant", false, listOf(
        "activa asistente local",
        "activa la asistente local",
        "activa el asistente local",
        "activa el asistente de local",
        "local"
    )),

    /**
     * Change to remote assistant
     */
    ACTIVATE_REMOTE_ASSISTANT(true, "Activate remote assistant", false, listOf(
        "activa asistente remoto",
        "activa la asistente remoto",
        "activa el asistente remoto",
        "activa el asistente de remoto",
        "remoto"
    )),

    /**
     * Create an alarm
     */
    CREATE_ALARM(true, "Create an alarm", false, listOf(
        "con una alarma en",
        "pon una alarma en"
    )),

    /**
     * Exit from app
     */
    EXIT(true, "Exit", false, listOf(
        "apágate",
        "apagate",
        "cierra el sistema"
    )),

    /**
     * Save car coordinates
     */
    SAVE_CAR_COORDINATES(true, "Save car coordinates", false, listOf(
    "guarda la ubicación",
    "aparca",
    "guarda las coordenadas",
    ))
}
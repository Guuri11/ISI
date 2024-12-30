package org.isi.domain.models

class ErrorMessage {

    companion object {
        const val ALARM_COMMAND_COULD_NOT_BE_INTERPRETED = "El comando de alarma no pudo ser interpretado"
        const val TIME_REQUESTED_UNIT_NOT_RECOGNIZED = "Cantidad de tiempo no reconocida o no soportada"
        const val CANT_PERFORM_REQUEST = "Error al hacer la solicitud: "
        const val UNIT_TIME_NOT_RECOGNIZED = "Unidad de tiempo no reconocida"
        const val CONNECTION_ERROR = "Error en la conexión: "
        const val CONNECTION_ERROR_TRYING_WITH_LOCAL_ASSISTANT = "El servidor no responde, probando con el asistente local"
        const val LANGUAGE_NOT_SUPPORTED = "Idioma no soportado"
        const val EXCEPTION_CACHED = "Excepción localizada"
        const val UNKNOWN_ERROR = "Error desconocido"
        // TODO this literal is used for a condition, refactor this
        const val CONNECTED_REFUSED = "Connection refused"
    }
}
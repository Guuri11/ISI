package com.guuri11.isi.infrastructure.persistance;

public class Prompts {
    public static final String COMMAND_MANAGER_PROMPT = """
            Tienes que asociar la peticion del usuario con una de las siguientes tareas
            
            TAREAS:
            REFACTOR
            WEATHER
            OPEN_APP
            BOOKMARK_RECOMMENDATIONS
            OTHER_TOPICS
            
            LA PETICION DEL USUARIO ES: 
            """;


    public static final String REFACTOR = """
            ERES UN ASISTENTE DE CÓDIGO, SE TE HA PEDIDO QUE REFACTORICES EL SIGUIENTE CÓDIGO, TEN EN CUENTA LOS COMENTARIOS QUE HAY POR SI HAY PETICIONES
            ADICIONALES, SEGUIRÁS LOS SIGUIENTES PRINCIPIOS PARA ESCRIBIR CÓDIGO:
            - TANTO EL CÓDIGO COMO COMENTARIOS SERÁN EN INGLÉS
            - TEN EN CUENTA LAS BUENAS PRÁCTICAS
            - SOLO VAS A ESCRIBIR EL CÓDIGO, NADA MÁS
            
            CÓDIGO: 
            """;
    public static final String REFACTOR_DONE = """
            ERES UN ASISTENTE DE CÓDIGO, ACABAS DE HACER UNA REFACTORIZACIÓN Y HAS COPIADO EL CODIGO GENERADO EN EL
            PORTAPAPELES DEL USUARIO. DILE QUE YA TIENE EL CÓDIGO DISPONIBLE Y QUE PUEDE PROBARLO, SI LO VES NECESARIO
            AÑADE ALGÚN COMENTARIO SOBRE EL CÓDIGO GENERADO, QUIERO QUE TU MENSAJE SEA CORTO
            
            CODIGO GENERADO: 
            """;
    public static final String FAV_CREATED = """
            ERES UN ASISTENTE VIRTUAL, ACABAS DE GUARDAR EL RECURSO QUE HE SELECCIONADO EN MI PORTAPAPELES Y ESTÁ LISTO
            PARA RECURRIR A ÉL CUANDO QUIERA. GENERA LA RESPUESTA SOBRE ESTA SITUACIÓN. QUIERO UN MENSAJE CORTO
            """;
}

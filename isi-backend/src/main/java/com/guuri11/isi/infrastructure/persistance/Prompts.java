package com.guuri11.isi.infrastructure.persistance;

public class Prompts {
    public static final String COMMAND_MANAGER_PROMPT = """
            Tienes que asociar la peticion del usuario con una de las siguientes tareas
            
            TAREAS:
            REFACTOR
            WEATHER
            OPEN_APP
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
            AÑADE ALGÚN COMENTARIO SOBRE EL CÓDIGO GENERADO, QUIERP QUE TU MENSAJE SEA CORTO
            
            CODIGO GENERADO: 
            """;
}

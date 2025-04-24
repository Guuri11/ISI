# Directorio `presentation`

El directorio `presentation` contiene la forma de presentar los datos. Estos presentaciones son responsables de recibir las solicitudes externas y traducirlas al lenguaje del dominio para que puedan ser procesadas por la lógica de negocio.

## Estructura

El directorio `presentation` está organizado para incluir varios tipos de adaptadores de entrada, por ejemplo:

- **Controladores HTTP**: Gestionan las solicitudes y respuestas HTTP, actuando como la interfaz principal para las APIs REST.
- **CLI**: Permite la interacción con la aplicación a través de comandos en la línea de comandos.
- **Playgrounds**: Espacios para probar y depurar funcionalidades específicas de la aplicación.

## Propósito

El objetivo principal del directorio `presentation` es encapsular toda la lógica relacionada con la recepción de solicitudes externas, asegurando una clara separación de responsabilidades y facilitando la mantenibilidad del proyecto.

## Mejores Prácticas

- Mantén la lógica en este directorio enfocada únicamente en la recepción y traducción de solicitudes externas.
- Evita mezclar la lógica de negocio con el código de los adaptadores de entrada.
- Asegúrate de manejar errores y validar las entradas correctamente antes de pasarlas a la capa de negocio.
- Escribe pruebas unitarias para simular solicitudes externas y validar el comportamiento de los adaptadores.

## Ejemplo

Aquí tienes un ejemplo de cómo podría estructurarse un adaptador:

```plaintext
├── rest-api/
│   ├── config/              # Configuración de la API REST
│   ├── router/              # Definición de rutas y controladores
│   └── main.rs              # Punto de entrada principal para la API REST
│
├── cli/
│   └── main.rs              # Punto de entrada principal para app CLI
│
├── playground/
│   └── main.rs              # Punto de entrada principal para un playground donde hacer pruebas rápidas
```

Cada archivo o módulo en este directorio debe centrarse en una interacción externa específica, asegurando claridad y facilidad de mantenimiento.
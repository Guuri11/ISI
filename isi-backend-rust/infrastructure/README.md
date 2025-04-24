# Directorio `infrastructure`

`infrastructure` contiene los adaptadores de entrada y salida del proyecto. Estos adaptadores son responsables de manejar las interacciones con sistemas externos, como bases de datos, APIs, Kafka u otros servicios. Actúan como un puente entre la lógica central de la aplicación y las dependencias externas, asegurando una clara separación de responsabilidades.

## Estructura

El directorio `infrastructure` está organizado para incluir varios tipos de adaptadores de salida:

- **Repositorios**: Gestionan las interacciones con bases de datos, como consultas, guardado o actualización de datos.
- **Consumidores**: Consumidores de Kafka escuchando eventos.
- **Integraciones con Servicios Externos**: Administran la comunicación con APIs de terceros o sistemas externos.
- **Otros Adaptadores**: Cualquier componente adicional necesario para interactuar con recursos externos.

## Propósito

El objetivo principal del directorio `infrastructure` es encapsular toda la lógica relacionada con sistemas externos, haciendo que la aplicación sea más modular y fácil de mantener. Al aislar estas responsabilidades, el proyecto sigue los principios de arquitectura limpia.

## Mejores Prácticas

- Mantén la lógica en este directorio enfocada únicamente en interacciones externas.
- Evita mezclar la lógica de negocio principal con el código de los adaptadores.
- Asegúrate de manejar errores y registrar eventos correctamente en las interacciones con sistemas externos.
- Escribe pruebas unitarias para simular dependencias externas y validar el comportamiento de los adaptadores.

## Ejemplo

Aquí tienes un ejemplo de cómo podría estructurarse un adaptador:

```plaintext
├── persistance/
│   ├── postgres/              # adaptador a Postgres
│   └── redis/                 # adaptador de Redis
│
│├── events/
│   ├── kafka/                 # adaptador a Kafka
│
├── notifications/
│   ├── sendrid/               # adaptador a Sengrid
│   ├── twilio/                # adaptador a Twilio
```

Cada archivo o módulo en este directorio debe centrarse en una interacción externa específica, asegurando claridad y facilidad de mantenimiento.
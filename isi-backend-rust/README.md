# Backend de ISI

## Estructura del proyecto
- **[presentation](presentation/README.md)**: Cómo vamos a presentar la lógica de negocio. Por ejemplo REST API, CLI, un cron, scripts, playgrounds...
- **[business](business/README.md)**: Incluye la lógica de negocio y las reglas del dominio. Aquí se encuentran los casos de uso y las entidades principales que definen el comportamiento del sistema.
- **[infrastructure](infrastructure/README.md)**: Contiene los adaptadores de salida y salida, como repositorios o integraciones con servicios externos, que permiten interactuar con bases de datos, APIs externas, Kafka u otros sistemas.

## Tecnologías clave
- **Rust**: Lenguaje de programación utilizado para el backend.
- **Axum**: Framework web para construir APIs.
- **SQLx**: Herramienta SQL asíncrona para interacciones con bases de datos.
- **PostgreSQL**: Base de datos utilizada para almacenar los datos de la aplicación.
- **dotenvy**: Para gestionar variables de entorno.
- **Tracing**: Para registro y observabilidad.

## Configuración e instalación

### Requisitos previos
- Rust (última versión estable)
- PostgreSQL
- Docker (opcional, para configuración en contenedores)

## Makefile
El proyecto incluye un `Makefile` con comandos útiles para:
- Compilar la aplicación
- Ejecutar migraciones de base de datos
- Formatear el código
- Realizar verificaciones de seguridad
- Iniciar contenedores Docker

Para ver todos los comandos disponibles, ejecuta:
```bash
make help
```
Esto proporcionará instrucciones detalladas sobre cómo realizar tareas como pruebas, linting y formateo.
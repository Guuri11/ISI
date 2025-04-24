# Directorio `business`

El directorio `business` incluye la lógica de negocio y las reglas del dominio. Aquí se encuentran los casos de uso y las entidades principales que definen el comportamiento del sistema.

## Estructura

El directorio `business` está organizado para incluir:

- **Casos de Uso**: Representan las operaciones principales que la aplicación puede realizar, encapsulando la lógica de negocio.
- **Entidades del Dominio**: Modelos que definen los conceptos principales del sistema y sus reglas asociadas.
- **Errores del Dominio**: Definición de errores específicos del dominio para un manejo adecuado de excepciones.

## Propósito

El objetivo principal del directorio `business` es centralizar toda la lógica de negocio, asegurando que las reglas del dominio estén claramente definidas y separadas de otras capas de la aplicación.

## Mejores Prácticas

- Mantén la lógica de negocio independiente de los adaptadores de entrada y salida.
- Define claramente las reglas del dominio y asegúrate de que sean consistentes en toda la aplicación.
- Escribe pruebas unitarias para validar los casos de uso y las entidades del dominio.

## Ejemplo

Aquí tienes un ejemplo de cómo podría estructurarse un caso de uso:

```plaintext
├── application/
│   ├── product/
│       ├── create_product.rs       # Caso de uso para crear un camión
│       ├── update_product.rs       # Caso de uso para actualizar un camión
│       └── delete_product.rs       # Caso de uso para eliminar un camión
├── domain/
│   ├── product/
│       ├── product.rs              # Entidad del dominio para camiones
│       └── product_errors.rs       # Errores específicos del dominio de camiones
│   └── errors.rs                 # Errores generales del dominio
```

Cada archivo o módulo en este directorio debe centrarse en una responsabilidad específica, asegurando claridad y facilidad de mantenimiento.
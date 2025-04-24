use sqlx::{PgPool, postgres::PgPoolOptions};
use std::{path::Path, time::Duration};
use thiserror::Error;

#[derive(Error, Debug)]
pub enum DatabaseError {
    #[error("Error de conexión a la base de datos: {0}")]
    ConnectionError(String),
    #[error("Error al ejecutar migración: {0}")]
    MigrationError(String),
}
/// Configuración para la conexión a la base de datos
pub struct DatabaseConfig {
    pub connection_string: String,
    pub max_connections: u32,
    pub acquire_timeout: Duration,
}

impl DatabaseConfig {
    /// Crea una nueva configuración de base de datos con valores por defecto
    pub fn new(connection_string: String) -> Self {
        Self {
            connection_string,
            max_connections: 5,
            acquire_timeout: Duration::from_secs(30),
        }
    }
}

/// Crea un pool de conexiones a la base de datos PostgreSQL
pub async fn create_postgres_pool(config: &DatabaseConfig) -> Result<PgPool, DatabaseError> {
    let pool = PgPoolOptions::new()
        .max_connections(config.max_connections)
        .acquire_timeout(config.acquire_timeout)
        .connect(&config.connection_string)
        .await
        .map_err(|e| DatabaseError::ConnectionError(e.to_string()))?;

    Ok(pool)
}
/// Ejecuta las migraciones de la base de datos desde el directorio especificado
pub async fn run_migrations(pool: &PgPool, migrations_path: &str) -> Result<(), DatabaseError> {
    let path = Path::new(migrations_path);

    // Verifica que el directorio de migraciones existe
    if !path.exists() {
        return Err(DatabaseError::MigrationError(format!(
            "El directorio de migraciones '{}' no existe",
            migrations_path
        )));
    }

    // Ejecuta las migraciones
    sqlx::migrate::Migrator::new(path)
        .await
        .map_err(|e| DatabaseError::MigrationError(e.to_string()))?
        .run(pool)
        .await
        .map_err(|e| DatabaseError::MigrationError(e.to_string()))
}

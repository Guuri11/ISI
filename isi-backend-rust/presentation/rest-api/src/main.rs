use persistance::postgres::db::{DatabaseConfig, create_postgres_pool};
use rest_api::{
    config::{settings::Settings, tracing::setup_tracing},
    router::core::{create_router, shutdown_signal},
};
use tokio::net::TcpListener;
use tracing::{error, info};

#[tokio::main]
async fn main() {
    if let Err(err) = run().await {
        error!("Error al ejecutar el servidor: {}", err);
    }
}

async fn run() -> Result<(), Box<dyn std::error::Error>> {
    setup_tracing();

    let settings = Settings::from_env()?;
    let pool = create_postgres_pool(&DatabaseConfig::new(settings.database_url.clone())).await?;
    let app = create_router(pool, settings.clone());
    
    let addr = format!("{}:{}", settings.service_host, settings.service_port);
    info!("🚀 ISI iniciado correctamente!");
    info!("📡 API disponible en: http://{}/", addr);

    let listener = TcpListener::bind(&addr).await?;
    axum::serve(listener, app)
        .with_graceful_shutdown(shutdown_signal())
        .await?;

    info!("🚀 ISI detenido!");
    Ok(())
}

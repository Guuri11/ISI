/// Herramienta para registrar eventos estructurados
/// y realizar diagnósticos en aplicaciones asincrónicas o concurrentes.
///
/// - `layer::SubscriberExt`: Proporciona extensiones para construir y combinar capas de tracing.
/// - `util::SubscriberInitExt`: Ofrece métodos para inicializar suscriptores de tracing.
///
/// Este archivo es clave para establecer cómo se registran y procesan los eventos de tracing
/// en la aplicación, lo que facilita la depuración y el monitoreo.
///
/// Documentación de `tracing subscriber`: https://docs.rs/tracing-subscriber/latest/tracing_subscriber/
use tracing_subscriber::{layer::SubscriberExt, util::SubscriberInitExt};

use super::settings::Settings;

pub fn setup_tracing(settings: Settings) {
    let env_filter = match settings.env.as_str() {
        "development" => "debug,sqlx=debug,tower_http=info,axum::rejection=trace",
        "production" => "info,sqlx=info,tower_http=info,axum::rejection=warn",
        _ => "debug,sqlx=debug,tower_http=info,axum::rejection=trace",
    };

    tracing_subscriber::registry()
        .with(tracing_subscriber::EnvFilter::new(env_filter))
        .with(
            tracing_subscriber::fmt::layer()
                .with_file(true)
                .with_line_number(true)
                .with_target(true)
                .with_span_events(tracing_subscriber::fmt::format::FmtSpan::CLOSE),
        )
        .init();
}

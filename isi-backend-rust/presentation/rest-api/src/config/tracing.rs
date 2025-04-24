/// Herramienta para registrar eventos estructurados
/// y realizar diagnósticos en aplicaciones asincrónicas o concurrentes.
///
/// - `layer::SubscriberExt`: Proporciona extensiones para construir y combinar capas de tracing.
/// - `util::SubscriberInitExt`: Ofrece métodos para inicializar suscriptores de tracing.
///
/// Este archivo es clave para establecer cómo se registran y procesan los eventos de tracing
/// en la aplicación, lo que facilita la depuración y el monitoreo.
use tracing_subscriber::{layer::SubscriberExt, util::SubscriberInitExt};

pub fn setup_tracing() {
    tracing_subscriber::registry()
        .with(
            tracing_subscriber::EnvFilter::try_from_default_env().unwrap_or_else(|_| {
                "debug,sqlx=debug,tower_http=info,axum::rejection=trace".into()
            }),
        )
        .with(
            tracing_subscriber::fmt::layer()
                .with_file(true)
                .with_line_number(true)
                .with_thread_ids(true)
                .with_thread_names(true)
                .with_target(true)
                .with_span_events(tracing_subscriber::fmt::format::FmtSpan::CLOSE),
        )
        .init();
}

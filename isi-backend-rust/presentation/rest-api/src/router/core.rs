use std::time::Duration;

use axum::{
    Router,
    body::{Body, Bytes},
    error_handling::HandleErrorLayer,
    extract::Request,
    http::{
        Method, StatusCode,
        header::{AUTHORIZATION, CONTENT_TYPE},
    },
    middleware::{self, Next},
    response::IntoResponse,
};
use http_body_util::BodyExt;

use sqlx::PgPool;
use tower::ServiceBuilder;
use tower_http::{
    cors::{Any, CorsLayer},
    trace::TraceLayer,
};
use tracing::info;
use utoipa::OpenApi;
use utoipa_swagger_ui::SwaggerUi;

use crate::{
    config::{app_state::AppState, settings::Settings},
    router::command::controller::CommandApiDoc,
};

use super::{
    command::controller::command_routes,
    exceptions::{RestError, handle_error},
};

fn create_swagger_ui() -> SwaggerUi {
    let swagger =
        SwaggerUi::new("/swagger-ui").url("/docs/commands/openapi.json", CommandApiDoc::openapi());

    info!("📚 Swagger UI: /swagger-ui");
    info!("🔎 OpenAPI JSON: /docs/commands/openapi.json");

    swagger
}

fn configure_cors() -> CorsLayer {
    CorsLayer::new()
        .allow_methods([Method::GET, Method::POST, Method::PUT, Method::DELETE])
        .allow_origin(Any) //TODO Review this
        .allow_headers([AUTHORIZATION, CONTENT_TYPE])
}

/// Router principal de la aplicación:
/// - Organizar rutas
/// - Aplicar middleware
/// - CORS
/// - Manejo de errores
/// - AppState
pub fn create_router(pool: PgPool, settings: Settings) -> Router {
    let state = AppState::new(pool, settings.clone());

    let cors = configure_cors();

    // Configura el middleware de la aplicación, incluyendo manejo de errores y tiempo de espera
    let middleware = ServiceBuilder::new()
        .layer(HandleErrorLayer::new(handle_error))
        .timeout(Duration::from_secs(30))
        .layer(cors)
        .layer(middleware::from_fn(print_request_response)); // Depuración

    let routes = Router::new().nest("/commands", command_routes());

    Router::new()
        .route("/health", axum::routing::get(health_check))
        .merge(routes)
        .merge(create_swagger_ui())
        .layer(
            TraceLayer::new_for_http()
                .make_span_with(|req: &axum::http::Request<_>| {
                    // Genera un span (lapso de tiempo) de trazado para cada solicitud, útil para monitoreo
                    tracing::info_span!(
                        "request",
                        method = %req.method(),
                        uri = %req.uri()
                    )
                })
                // Registra la duración de la solicitud
                .on_response(
                    |response: &axum::http::Response<_>,
                     latency: std::time::Duration,
                     _span: &tracing::Span| {
                        tracing::info!(
                            "request completed: status = {status}, latency = {latency:?}",
                            status = response.status(),
                            latency = latency
                        ); // Registra información sobre la respuesta, incluyendo latencia
                    },
                )
                // Registra errores del servidor
                .on_failure(
                    |error: tower_http::classify::ServerErrorsFailureClass,
                     latency: std::time::Duration,
                     _span: &tracing::Span| {
                        tracing::error!("request failed: error = {error:?}, latency = {latency:?}");
                    },
                ),
        )
        .fallback(fallback) // Ruta por defecto para manejar solicitudes no reconocidas
        .layer(middleware) // Aplica el middleware configurado
        .with_state(state) // Asocia el estado compartido con el router
}

/// Ruta para verificar el estado de la aplicación, útil para monitoreo y pruebas rápidas
async fn health_check() -> &'static str {
    "OK\n"
}

/// Ruta por defecto para manejar solicitudes no reconocidas, devuelve un error 404
pub async fn fallback() -> Result<impl IntoResponse, RestError> {
    Ok((StatusCode::NOT_FOUND, "Not Found"))
}

/// Middleware para registrar solicitudes y respuestas, útil para depuración y auditoría
async fn print_request_response(
    req: Request,
    next: Next,
) -> Result<impl IntoResponse, (StatusCode, String)> {
    let (parts, body) = req.into_parts();
    let bytes = buffer_and_print("request", body).await?; // Registra el cuerpo de la solicitud
    let req = Request::from_parts(parts, Body::from(bytes));

    let res = next.run(req).await;

    Ok(res)
}

/// Función auxiliar para registrar el cuerpo de solicitudes y respuestas
async fn buffer_and_print<B>(direction: &str, body: B) -> Result<Bytes, (StatusCode, String)>
where
    B: axum::body::HttpBody<Data = Bytes>,
    B::Error: std::fmt::Display,
{
    let bytes = match body.collect().await {
        Ok(collected) => collected.to_bytes(),
        Err(err) => {
            return Err((
                StatusCode::BAD_REQUEST,
                format!("failed to read {direction} body: {err}"),
            ));
        }
    };

    if let Ok(body) = std::str::from_utf8(&bytes) {
        tracing::debug!("{direction} body = {body:?}");
    }

    Ok(bytes)
}

/// Señal para apagar la aplicación de forma controlada
pub async fn shutdown_signal() {
    tokio::signal::ctrl_c()
        .await
        .expect("Failed to install CTRL+C signal handler"); // Manejo de señal CTRL+C para apagar la aplicación
}

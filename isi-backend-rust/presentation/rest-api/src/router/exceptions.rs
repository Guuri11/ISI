use axum::{
    BoxError, Json,
    http::StatusCode,
    response::{IntoResponse, Response},
};
use business::domain::{
    chat::errors::ChatError, command::errors::CommandError, fav::errors::FavError,
};
use serde::Serialize;
use thiserror::Error;
use tracing::error;

#[derive(Serialize)]
struct ErrorResponse {
    error: String,
    message: String,
}

#[derive(Debug, Error)]
pub enum RestError {
    #[error("Command error")]
    Command(#[from] CommandError),
    #[error("Fav error")]
    Fav(#[from] FavError),
    #[error("Chat error")]
    Chat(#[from] ChatError),
    #[error("Error desconocido")]
    Unknown(#[from] anyhow::Error),
}

/**
 * This is a custom error handler for the RestError enum.
 * It implements the IntoResponse trait from axum, allowing us to convert
 * the error into an HTTP response.
 */
impl IntoResponse for RestError {
    fn into_response(self) -> Response {
        match self {
            RestError::Command(entity_error) => {
                let error_response = ErrorResponse {
                    error: "CommandError".to_string(),
                    message: entity_error.to_string(),
                };
                (StatusCode::BAD_REQUEST, Json(error_response)).into_response()
            }
            RestError::Fav(entity_error) => {
                let error_response = ErrorResponse {
                    error: "FavError".to_string(),
                    message: entity_error.to_string(),
                };
                (StatusCode::BAD_REQUEST, Json(error_response)).into_response()
            }
            RestError::Chat(entity_error) => {
                let error_response = ErrorResponse {
                    error: "CommandError".to_string(),
                    message: entity_error.to_string(),
                };
                (StatusCode::BAD_REQUEST, Json(error_response)).into_response()
            }
            RestError::Unknown(error) => {
                let error_response = ErrorResponse {
                    error: "InternalServerError".to_string(),
                    message: error.to_string(),
                };
                (StatusCode::INTERNAL_SERVER_ERROR, Json(error_response)).into_response()
            }
        }
    }
}

/**
 * This function handles errors that are not of type RestError.
 * It returns a generic error response with a 500 status code.
 */
pub async fn handle_error(error: BoxError) -> impl IntoResponse {
    let status = if error.is::<tower::timeout::error::Elapsed>() {
        StatusCode::REQUEST_TIMEOUT
    } else {
        StatusCode::INTERNAL_SERVER_ERROR
    };

    let message = error.to_string();
    error!(?status, %message, "Request failed");

    (
        status,
        axum::Json(ErrorResponse {
            error: "Unhandled error".to_string(),
            message,
        }),
    )
}

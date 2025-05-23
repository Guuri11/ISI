use thiserror::Error;

#[derive(Debug, Error)]
pub enum ScreenshotError {
    #[error("screenshot.not_found")]
    NotFound(String),
    #[error("screenshot.service_error")]
    ServiceError(String),
    #[error("screenshot.unknown")]
    Unknown(#[from] anyhow::Error),
}

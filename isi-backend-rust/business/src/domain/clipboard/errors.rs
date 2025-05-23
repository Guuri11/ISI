use thiserror::Error;

#[derive(Debug, Error)]
pub enum ClipboardError {
    #[error("clipboard.not_found")]
    NotFound(String),
    #[error("clipboard.service_error")]
    ServiceError(String),
    #[error("clipboard.unknown")]
    Unknown(#[from] anyhow::Error),
}

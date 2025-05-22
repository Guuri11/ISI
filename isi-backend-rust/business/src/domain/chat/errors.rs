use thiserror::Error;

use crate::domain::errors::RepositoryError;

#[derive(Debug, Error)]
pub enum ChatError {
    #[error("chat.not_found")]
    NotFound(String),
    #[error("chat.already_exists")]
    DuplicateChat(String),
    #[error("chat.repository_error")]
    RepositoryError(String),
    #[error("chat.validation_error")]
    ValidationError(String),
    #[error("chat.unknown")]
    Unknown(#[from] anyhow::Error),
}

/**
 * Matches the RepositoryError to the ChatError.
 */
impl From<RepositoryError> for ChatError {
    fn from(err: RepositoryError) -> Self {
        match err {
            RepositoryError::NotFound(msg) => Self::NotFound(msg),
            RepositoryError::Persistence(msg) => Self::RepositoryError(msg),
            RepositoryError::DatabaseError(msg) => Self::RepositoryError(msg),
            RepositoryError::Duplicated(msg) => Self::DuplicateChat(msg),
        }
    }
}

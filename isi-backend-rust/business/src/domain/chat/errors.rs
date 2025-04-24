use thiserror::Error;

use crate::domain::errors::RepositoryError;

#[derive(Debug, Error)]
pub enum ChatError {
    #[error("Chat not found")]
    NotFound(String),
    #[error("Chat already exists")]
    DuplicateChat(String),
    #[error("Some error occurred while processing the request")]
    RepositoryError(String),
    #[error("Validation error")]
    Validation(String),
    #[error("Unknown error occurred")]
    Unknown(#[from] anyhow::Error),
}

/**
 * Matches the RepositoryError to the ChatError.
 */
impl From<RepositoryError> for ChatError {
    fn from(err: RepositoryError) -> Self {
        match err {
            RepositoryError::NotFound(msg) => Self::NotFound(msg),
            RepositoryError::DuplicateEntity(entity) => Self::DuplicateChat(entity),
            RepositoryError::Persistence(msg) => Self::RepositoryError(msg),
            RepositoryError::DatabaseError(msg) => Self::RepositoryError(msg),
        }
    }
}

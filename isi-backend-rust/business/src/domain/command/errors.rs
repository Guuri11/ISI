use thiserror::Error;

use crate::domain::{errors::RepositoryError, task::errors::TaskError};

#[derive(Debug, Error)]
pub enum CommandError {
    #[error("Command not found: {0}")]
    NotFound(String),
    #[error("Command already exists: {0}")]
    DuplicateCommand(String),
    #[error("Repository error: {0}")]
    RepositoryError(String),
    #[error("Validation error: {0}")]
    Validation(String),
    #[error("Error processing task: {0}")]
    TaskError(#[from] TaskError),
    #[error("Unknown error occurred: {0}")]
    Unknown(#[from] anyhow::Error),
}

/**
 * Matches the RepositoryError to the CommandError.
 */
impl From<RepositoryError> for CommandError {
    fn from(err: RepositoryError) -> Self {
        match err {
            RepositoryError::NotFound(msg) => Self::NotFound(msg),
            RepositoryError::Persistence(msg) => Self::RepositoryError(msg),
            RepositoryError::DatabaseError(msg) => Self::RepositoryError(msg),
            RepositoryError::DuplicateEntity(entity) => Self::DuplicateCommand(entity),
        }
    }
}

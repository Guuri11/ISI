use thiserror::Error;

use crate::domain::{errors::RepositoryError, task::errors::TaskError};

#[derive(Debug, Error)]
pub enum CommandError {
    #[error("command.not_found")]
    NotFound(String),
    #[error("command.already_exists")]
    DuplicateCommand(String),
    #[error("command.repository_error")]
    RepositoryError(String),
    #[error("command.validation_error")]
    ValidationError(String),
    #[error("command.task_error")]
    TaskError(#[from] TaskError),
    #[error("command.unknown")]
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
            RepositoryError::Duplicated(msg) => Self::DuplicateCommand(msg),
        }
    }
}

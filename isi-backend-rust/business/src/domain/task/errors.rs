use thiserror::Error;

use crate::domain::errors::RepositoryError;

#[derive(Debug, Error)]
pub enum TaskError {
    #[error("task.not_found")]
    NotFound(String),
    #[error("task.already_exists")]
    DuplicateTask(String),
    #[error("task.repository_error")]
    RepositoryError(String),
    #[error("task.validation_error")]
    ValidationError(String),
    #[error("task.execution_failed")]
    ExecutionFailed(String),
    #[error("task.unknown")]
    Unknown(#[from] anyhow::Error),
}

/**
 * Matches the RepositoryError to the TaskError.
 */
impl From<RepositoryError> for TaskError {
    fn from(err: RepositoryError) -> Self {
        match err {
            RepositoryError::NotFound(msg) => Self::NotFound(msg),
            RepositoryError::Persistence(msg) => Self::RepositoryError(msg),
            RepositoryError::DatabaseError(msg) => Self::RepositoryError(msg),
            RepositoryError::Duplicated(msg) => Self::DuplicateTask(msg),
        }
    }
}

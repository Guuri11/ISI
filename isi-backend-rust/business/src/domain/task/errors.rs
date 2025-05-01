use thiserror::Error;

use crate::domain::errors::RepositoryError;

#[derive(Debug, Error)]
pub enum TaskError {
    #[error("Task not found: {0}")]
    NotFound(String),
    #[error("Execution failed: {0}")]
    ExecutionFailed(String),
    #[error("Unknown error occurred: {0}")]
    Unknown(#[from] anyhow::Error),
}

impl From<RepositoryError> for TaskError {
    fn from(err: RepositoryError) -> Self {
        match err {
            RepositoryError::NotFound(msg) => Self::NotFound(msg),
            RepositoryError::DuplicateEntity(entity) => Self::ExecutionFailed(entity),
            RepositoryError::Persistence(msg) => Self::ExecutionFailed(msg),
            RepositoryError::DatabaseError(msg) => Self::ExecutionFailed(msg),
        }
    }
}

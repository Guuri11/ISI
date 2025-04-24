use thiserror::Error;

use crate::domain::errors::RepositoryError;

#[derive(Debug, Error)]
pub enum FavError {
    #[error("Fav not found")]
    NotFound(String),
    #[error("Some error occurred while processing the request")]
    RepositoryError(String),
    #[error("Fav already exists")]
    DuplicateFav(String),
    #[error("Validation error")]
    Validation(String),
    #[error("Unknown error occurred")]
    Unknown(#[from] anyhow::Error),
}

/**
 * Matches the RepositoryError to the FavError.
 */
impl From<RepositoryError> for FavError {
    fn from(err: RepositoryError) -> Self {
        match err {
            RepositoryError::NotFound(msg) => Self::NotFound(msg),
            RepositoryError::Persistence(msg) => Self::RepositoryError(msg),
            RepositoryError::DuplicateEntity(entity) => Self::DuplicateFav(entity),
            RepositoryError::DatabaseError(msg) => Self::RepositoryError(msg),
        }
    }
}

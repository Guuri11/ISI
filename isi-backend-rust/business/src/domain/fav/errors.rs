use thiserror::Error;

use crate::domain::{errors::RepositoryError, screenshot::errors::ScreenshotError};

#[derive(Debug, Error)]
pub enum FavError {
    #[error("fav.not_found")]
    NotFound(String),
    #[error("fav.already_exists")]
    DuplicateFav(String),
    #[error("fav.repository_error")]
    RepositoryError(String),
    #[error("fav.validation_error")]
    ValidationError(String),
    #[error("fav.screenshot_error")]
    ScreenshotError(#[from] ScreenshotError),
    #[error("fav.unknown")]
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
            RepositoryError::DatabaseError(msg) => Self::RepositoryError(msg),
            RepositoryError::Duplicated(msg) => Self::DuplicateFav(msg),
        }
    }
}

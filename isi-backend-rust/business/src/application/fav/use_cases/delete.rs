use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;
use uuid::Uuid;

use crate::domain::fav::{
    errors::FavError,
    repository::FavRepository,
    use_cases::delete::{DeleteFavUseCase, DeleteFavUseCaseImpl},
};

impl DeleteFavUseCaseImpl {
    pub fn new(repository: Arc<dyn FavRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl DeleteFavUseCase for DeleteFavUseCaseImpl {
    async fn execute(&self, id: &Uuid) -> Result<(), FavError> {
        info!("Eliminación de fav");

        self.repository.delete(id).await.map_err(|e| e.into())
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::{
        domain::{
            errors::RepositoryError,
            fav::{errors::FavError, repository::FavRepository},
        },
        tests::mocks::mock_fav_repository::MockFavRepository,
    };
    use std::sync::Arc;

    // Helper para construir la usecase con el mock
    fn build_use_case(mock: Arc<dyn FavRepository>) -> DeleteFavUseCaseImpl {
        DeleteFavUseCaseImpl::new(mock)
    }

    #[tokio::test]
    async fn should_delete_fav_successfully() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_delete().returning(|_| Ok(()));
        let use_case = build_use_case(Arc::new(mock));
        let id = Uuid::new_v4();

        // Act
        let result = use_case.execute(&id).await;

        // Assert
        assert!(result.is_ok());
    }

    #[tokio::test]
    async fn should_fail_when_not_found() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_delete()
            .returning(|_| Err(RepositoryError::NotFound("not_found".to_string())));
        let use_case = build_use_case(Arc::new(mock));
        let id = Uuid::new_v4();

        // Act
        let result = use_case.execute(&id).await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(FavError::NotFound(_))));
    }
    #[tokio::test]
    async fn should_fail_when_repository_returns_error() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_delete()
            .returning(|_| Err(RepositoryError::Persistence("DB fail".into())));
        let use_case = build_use_case(Arc::new(mock));
        let id = Uuid::new_v4();

        // Act
        let result = use_case.execute(&id).await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(FavError::RepositoryError(_))));
    }
}

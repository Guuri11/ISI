use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;

use crate::domain::fav::{
    errors::FavError,
    model::Fav,
    repository::FavRepository,
    use_cases::update::{UpdateFavUseCase, UpdateFavUseCaseImpl},
};

impl UpdateFavUseCaseImpl {
    pub fn new(repository: Arc<dyn FavRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl UpdateFavUseCase for UpdateFavUseCaseImpl {
    async fn execute(&self, fav: &Fav) -> Result<Fav, FavError> {
        info!("Actualización de fav");

        self.repository
            .update(fav)
            .await
            .map_err(|e| e.into())
            .map(|_| fav.clone())
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
    fn build_use_case(mock: Arc<dyn FavRepository>) -> UpdateFavUseCaseImpl {
        UpdateFavUseCaseImpl::new(mock)
    }

    #[tokio::test]
    async fn should_update_fav_successfully() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_update().returning(|_| Ok(()));
        let use_case = build_use_case(Arc::new(mock));
        let entity = Fav::new("example".to_string()).unwrap();

        // Act
        let result = use_case.execute(&entity).await;

        // Assert
        assert!(result.is_ok());
        let updated_entity = result.unwrap();
        assert_eq!(updated_entity.id(), entity.id());
    }

    #[tokio::test]
    async fn should_fail_when_not_found() {
        // Arrange
        let entity = Fav::new("example".to_string()).unwrap();
        let mut mock = MockFavRepository::new();
        mock.expect_update()
            .returning(|_| Err(RepositoryError::NotFound("not_found".to_string())));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute(&entity).await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(FavError::NotFound(_))));
    }
    #[tokio::test]
    async fn should_fail_when_repository_returns_error() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_update()
            .returning(|_| Err(RepositoryError::Persistence("DB fail".into())));
        let use_case = build_use_case(Arc::new(mock));
        let entity = Fav::new("example".to_string()).unwrap();

        // Act
        let result = use_case.execute(&entity).await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(FavError::RepositoryError(_))));
    }
}

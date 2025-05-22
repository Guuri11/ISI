use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;

use crate::domain::fav::{
    errors::FavError,
    model::Fav,
    repository::FavRepository,
    use_cases::get_by::{GetByFavUseCase, GetByFavUseCaseImpl},
};

impl GetByFavUseCaseImpl {
    pub fn new(repository: Arc<dyn FavRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl GetByFavUseCase for GetByFavUseCaseImpl {
    async fn execute(&self) -> Result<Vec<Fav>, FavError> {
        info!("Obteniendo fav");

        self.repository.find_all().await.map_err(|e| e.into())
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
    fn build_use_case(mock: Arc<dyn FavRepository>) -> GetByFavUseCaseImpl {
        GetByFavUseCaseImpl::new(mock)
    }

    #[tokio::test]
    async fn should_get_fav_successfully() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_find_all()
            .returning(|| Ok(vec![Fav::new("example".to_string()).unwrap()]));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute().await;

        // Assert
        assert!(result.is_ok());
        let found_entities = result.unwrap();
        assert_eq!(found_entities.len(), 1);
        assert_eq!(found_entities[0].name(), "example");
    }

    #[tokio::test]
    async fn should_return_empty_list_when_not_found() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_find_all().returning(|| Ok(vec![]));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute().await;

        // Assert
        assert!(result.is_ok());
        let found_entities = result.unwrap();
        assert_eq!(found_entities.len(), 0);
    }
    #[tokio::test]
    async fn should_fail_when_repository_returns_error() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_find_all()
            .returning(|| Err(RepositoryError::Persistence("DB fail".into())));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute().await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(FavError::RepositoryError(_))));
    }
}

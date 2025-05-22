use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;

use crate::domain::fav::{
    errors::FavError,
    model::Fav,
    repository::FavRepository,
    use_cases::create::{CreateFavUseCase, CreateFavUseCaseImpl},
};

impl CreateFavUseCaseImpl {
    pub fn new(repository: Arc<dyn FavRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl CreateFavUseCase for CreateFavUseCaseImpl {
    async fn execute(&self, name: String) -> Result<Fav, FavError> {
        info!("Creación de fav");
        let entity = Fav::new(name)?;

        self.repository.save(&entity).await?;

        Ok(entity)
    }
}

#[cfg(test)]
mod tests {
    use crate::tests::mocks::mock_fav_repository::MockFavRepository;

    use super::*;
    use std::sync::Arc;

    // Helper para construir la usecase con el mock
    fn build_use_case(mock: Arc<dyn FavRepository>) -> CreateFavUseCaseImpl {
        CreateFavUseCaseImpl::new(mock)
    }

    #[tokio::test]
    async fn should_create_fav_successfully() {
        // Arrange
        let mut mock = MockFavRepository::new();
        mock.expect_save().returning(|_| Ok(()));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute("example".to_string()).await;

        // Assert
        assert!(result.is_ok());
        let created_entity = result.unwrap();
        assert_eq!(created_entity.name(), "example");
    }

    // TODO: Implementar el test de error
    #[tokio::test]
    async fn should_fail_when_condition() {
        // Arrange
        // Act
        // Assert
    }
}

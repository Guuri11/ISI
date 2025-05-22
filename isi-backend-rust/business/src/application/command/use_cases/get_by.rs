use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;

use crate::domain::command::{
    errors::CommandError,
    model::Command,
    repository::CommandRepository,
    use_cases::get_by::{GetByCommandUseCase, GetByCommandUseCaseImpl},
};

impl GetByCommandUseCaseImpl {
    pub fn new(repository: Arc<dyn CommandRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl GetByCommandUseCase for GetByCommandUseCaseImpl {
    async fn execute(&self) -> Result<Vec<Command>, CommandError> {
        info!("Obteniendo command");

        self.repository.find_all().await.map_err(|e| e.into())
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::{
        domain::{
            command::{errors::CommandError, repository::CommandRepository},
            errors::RepositoryError,
        },
        tests::mocks::mock_command_repository::MockCommandRepository,
    };
    use std::sync::Arc;

    // Helper para construir la usecase con el mock
    fn build_use_case(mock: Arc<dyn CommandRepository>) -> GetByCommandUseCaseImpl {
        GetByCommandUseCaseImpl::new(mock)
    }

    // TODO: Implementar los tests
    #[tokio::test]
    async fn should_get_command_successfully() {
        // Arrange
        // Act
        // Assert
    }

    #[tokio::test]
    async fn should_return_empty_list_when_not_found() {
        // Arrange
        let mut mock = MockCommandRepository::new();
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
        let mut mock = MockCommandRepository::new();
        mock.expect_find_all()
            .returning(|| Err(RepositoryError::Persistence("DB fail".into())));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute().await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(CommandError::RepositoryError(_))));
    }
}

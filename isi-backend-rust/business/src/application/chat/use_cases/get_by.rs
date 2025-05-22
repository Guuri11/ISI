use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;

use crate::domain::chat::{
    errors::ChatError,
    model::Chat,
    repository::ChatRepository,
    use_cases::get_by::{GetByChatUseCase, GetByChatUseCaseImpl},
};

impl GetByChatUseCaseImpl {
    pub fn new(repository: Arc<dyn ChatRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl GetByChatUseCase for GetByChatUseCaseImpl {
    async fn execute(&self) -> Result<Vec<Chat>, ChatError> {
        info!("Obteniendo chat");

        self.repository.find_all().await.map_err(|e| e.into())
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::{
        domain::{
            chat::{errors::ChatError, repository::ChatRepository},
            errors::RepositoryError,
        },
        tests::mocks::mock_chat_repository::MockChatRepository,
    };
    use std::sync::Arc;

    // Helper para construir la usecase con el mock
    fn build_use_case(mock: Arc<dyn ChatRepository>) -> GetByChatUseCaseImpl {
        GetByChatUseCaseImpl::new(mock)
    }

    #[tokio::test]
    async fn should_get_chat_successfully() {
        // Arrange
        let mut mock = MockChatRepository::new();
        mock.expect_find_all()
            .returning(|| Ok(vec![Chat::new().unwrap()]));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute().await;

        // Assert
        assert!(result.is_ok());
        let found_entities = result.unwrap();
        assert_eq!(found_entities.len(), 1);
        // assert_eq!(found_entities[0].id(), entity.id());
    }

    #[tokio::test]
    async fn should_return_empty_list_when_not_found() {
        // Arrange
        let mut mock = MockChatRepository::new();
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
        let mut mock = MockChatRepository::new();
        mock.expect_find_all()
            .returning(|| Err(RepositoryError::Persistence("DB fail".into())));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute().await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(ChatError::RepositoryError(_))));
    }
}

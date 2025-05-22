use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;
use uuid::Uuid;

use crate::domain::chat::{
    errors::ChatError,
    repository::ChatRepository,
    use_cases::delete::{DeleteChatUseCase, DeleteChatUseCaseImpl},
};

impl DeleteChatUseCaseImpl {
    pub fn new(repository: Arc<dyn ChatRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl DeleteChatUseCase for DeleteChatUseCaseImpl {
    async fn execute(&self, id: &Uuid) -> Result<(), ChatError> {
        info!("Eliminación de chat");

        self.repository.delete(id).await.map_err(|e| e.into())
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
    fn build_use_case(mock: Arc<dyn ChatRepository>) -> DeleteChatUseCaseImpl {
        DeleteChatUseCaseImpl::new(mock)
    }

    #[tokio::test]
    async fn should_delete_chat_successfully() {
        // Arrange
        let mut mock = MockChatRepository::new();
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
        let mut mock = MockChatRepository::new();
        mock.expect_delete()
            .returning(|_| Err(RepositoryError::NotFound("not_found".to_string())));
        let use_case = build_use_case(Arc::new(mock));
        let id = Uuid::new_v4();

        // Act
        let result = use_case.execute(&id).await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(ChatError::NotFound(_))));
    }
    #[tokio::test]
    async fn should_fail_when_repository_returns_error() {
        // Arrange
        let mut mock = MockChatRepository::new();
        mock.expect_delete()
            .returning(|_| Err(RepositoryError::Persistence("DB fail".into())));
        let use_case = build_use_case(Arc::new(mock));
        let id = Uuid::new_v4();

        // Act
        let result = use_case.execute(&id).await;

        // Assert
        assert!(result.is_err());
        assert!(matches!(result, Err(ChatError::RepositoryError(_))));
    }
}

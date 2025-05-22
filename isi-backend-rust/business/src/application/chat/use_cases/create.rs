use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;

use crate::domain::chat::{
    errors::ChatError,
    model::Chat,
    repository::ChatRepository,
    use_cases::create::{CreateChatUseCase, CreateChatUseCaseImpl},
};

impl CreateChatUseCaseImpl {
    pub fn new(repository: Arc<dyn ChatRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl CreateChatUseCase for CreateChatUseCaseImpl {
    async fn execute(&self) -> Result<Chat, ChatError> {
        info!("Creación de chat");
        let entity = Chat::new()?;

        self.repository.save(&entity).await?;

        Ok(entity)
    }
}

// TODO: tests
#[cfg(test)]
mod tests {
    use crate::tests::mocks::mock_chat_repository::MockChatRepository;

    use super::*;
    use std::sync::Arc;

    // Helper para construir la usecase con el mock
    fn build_use_case(mock: Arc<dyn ChatRepository>) -> CreateChatUseCaseImpl {
        CreateChatUseCaseImpl::new(mock)
    }

    #[tokio::test]
    async fn should_create_chat_successfully() {
        // Arrange
        let mut mock = MockChatRepository::new();
        mock.expect_save().returning(|_| Ok(()));
        let use_case = build_use_case(Arc::new(mock));

        // Act
        let result = use_case.execute().await;

        // Assert
        assert!(result.is_ok());
        // assert_eq!(created_entity.id(), entity.id());
    }

    #[tokio::test]
    async fn should_fail_when_condition() {
        // Arrange
        // Act
        // Assert
    }
}

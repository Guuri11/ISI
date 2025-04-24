use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use crate::domain::chat::{
    errors::ChatError,
    model::Chat,
    repository::ChatRepository,
    use_cases::{ChatUseCases, ChatUseCasesImpl},
};

impl ChatUseCasesImpl {
    pub fn new(repository: Arc<dyn ChatRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl ChatUseCases for ChatUseCasesImpl {
    async fn delete_chat(&self, chat_id: &Uuid) -> Result<(), ChatError> {
        self.repository.delete(chat_id).await.map_err(|e| e.into())
    }

    async fn get_chat_by_id(&self, chat_id: &Uuid) -> Result<Chat, ChatError> {
        self.repository
            .find_by_id(chat_id)
            .await
            .map_err(|e| e.into())
    }

    async fn get_chats(&self) -> Result<Vec<Chat>, ChatError> {
        self.repository.find_all().await.map_err(|e| e.into())
    }

    async fn register_chat(&self) -> Result<Chat, ChatError> {
        let chat = Chat::new().map_err(ChatError::Validation)?;

        self.repository.save(&chat).await?;

        Ok(chat)
    }

    async fn update_chat(&self, chat: &Chat) -> Result<(), ChatError> {
        self.repository.update(chat).await.map_err(|e| e.into())
    }
}

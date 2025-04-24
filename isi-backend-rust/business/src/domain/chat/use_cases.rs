use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use super::{errors::ChatError, model::Chat, repository::ChatRepository};

#[async_trait]
pub trait ChatUseCases: Send + Sync {
    async fn delete_chat(&self, chat_id: &Uuid) -> Result<(), ChatError>;
    async fn get_chats(&self) -> Result<Vec<Chat>, ChatError>;
    async fn get_chat_by_id(&self, chat_id: &Uuid) -> Result<Chat, ChatError>;
    async fn register_chat(&self) -> Result<Chat, ChatError>;
    async fn update_chat(&self, chat: &Chat) -> Result<(), ChatError>;
}

pub struct ChatUseCasesImpl {
    pub repository: Arc<dyn ChatRepository + Send + Sync>,
}

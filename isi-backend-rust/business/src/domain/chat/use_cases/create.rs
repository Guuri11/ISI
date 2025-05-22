use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::chat::{
    errors::ChatError,
    model::Chat,
    repository::ChatRepository,
};

#[async_trait]
pub trait CreateChatUseCase: Send + Sync {
    async fn execute(&self) -> Result<Chat, ChatError>;
}

pub struct CreateChatUseCaseImpl {
    pub repository: Arc<dyn ChatRepository + Send + Sync>,
}
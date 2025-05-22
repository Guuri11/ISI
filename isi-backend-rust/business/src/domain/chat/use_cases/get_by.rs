use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::chat::{errors::ChatError, model::Chat, repository::ChatRepository};

#[async_trait]
pub trait GetByChatUseCase: Send + Sync {
    async fn execute(&self) -> Result<Vec<Chat>, ChatError>;
}

pub struct GetByChatUseCaseImpl {
    pub repository: Arc<dyn ChatRepository + Send + Sync>,
}

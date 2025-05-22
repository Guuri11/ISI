use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use crate::domain::chat::{errors::ChatError, repository::ChatRepository};

#[async_trait]
pub trait DeleteChatUseCase: Send + Sync {
    async fn execute(&self, id: &Uuid) -> Result<(), ChatError>;
}

pub struct DeleteChatUseCaseImpl {
    pub repository: Arc<dyn ChatRepository + Send + Sync>,
}

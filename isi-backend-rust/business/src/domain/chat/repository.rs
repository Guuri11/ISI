use async_trait::async_trait;
use uuid::Uuid;

use crate::domain::errors::RepositoryError;

use super::model::Chat;

#[async_trait]
pub trait ChatRepository: Send + Sync {
    async fn save(&self, chat: &Chat) -> Result<(), RepositoryError>;
    async fn find_all(&self) -> Result<Vec<Chat>, RepositoryError>;
    async fn find_by_id(&self, id: &Uuid) -> Result<Chat, RepositoryError>;
    async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError>;
    async fn update(&self, chat: &Chat) -> Result<(), RepositoryError>;
}

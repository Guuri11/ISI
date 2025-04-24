use async_trait::async_trait;
use uuid::Uuid;

use crate::domain::errors::RepositoryError;

use super::model::Command;

#[async_trait]
pub trait CommandRepository: Send + Sync {
    async fn save(&self, command: &Command) -> Result<(), RepositoryError>;
    async fn find_all(&self) -> Result<Vec<Command>, RepositoryError>;
    async fn find_by_id(&self, id: &Uuid) -> Result<Command, RepositoryError>;
    async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError>;
    async fn update(&self, command: &Command) -> Result<(), RepositoryError>;
}

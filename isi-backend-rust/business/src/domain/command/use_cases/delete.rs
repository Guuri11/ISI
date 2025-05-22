use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use crate::domain::command::{errors::CommandError, repository::CommandRepository};

#[async_trait]
pub trait DeleteCommandUseCase: Send + Sync {
    async fn execute(&self, id: &Uuid) -> Result<(), CommandError>;
}

pub struct DeleteCommandUseCaseImpl {
    pub repository: Arc<dyn CommandRepository + Send + Sync>,
}

use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::command::{errors::CommandError, model::Command, repository::CommandRepository};

#[async_trait]
pub trait UpdateCommandUseCase: Send + Sync {
    async fn execute(&self, entity: &Command) -> Result<Command, CommandError>;
}

pub struct UpdateCommandUseCaseImpl {
    pub repository: Arc<dyn CommandRepository + Send + Sync>,
}

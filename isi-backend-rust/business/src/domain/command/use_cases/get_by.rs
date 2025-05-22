use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::command::{errors::CommandError, model::Command, repository::CommandRepository};

#[async_trait]
pub trait GetByCommandUseCase: Send + Sync {
    async fn execute(&self) -> Result<Vec<Command>, CommandError>;
}

pub struct GetByCommandUseCaseImpl {
    pub repository: Arc<dyn CommandRepository + Send + Sync>,
}

use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;

use crate::domain::command::{
    errors::CommandError,
    model::Command,
    repository::CommandRepository,
    use_cases::update::{UpdateCommandUseCase, UpdateCommandUseCaseImpl},
};

impl UpdateCommandUseCaseImpl {
    pub fn new(repository: Arc<dyn CommandRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl UpdateCommandUseCase for UpdateCommandUseCaseImpl {
    async fn execute(&self, command: &Command) -> Result<Command, CommandError> {
        info!("Actualización de command");

        self.repository
            .update(command)
            .await
            .map_err(|e| e.into())
            .map(|_| command.clone())
    }
}

// TODO: Tests

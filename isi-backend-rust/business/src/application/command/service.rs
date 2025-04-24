use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use crate::domain::command::{
    errors::CommandError,
    model::Command,
    repository::CommandRepository,
    use_cases::{CommandUseCases, CommandUseCasesImpl},
    value_objets::{ChatId, MessageType, Task},
};

impl CommandUseCasesImpl {
    pub fn new(repository: Arc<dyn CommandRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl CommandUseCases for CommandUseCasesImpl {
    async fn delete_command(&self, command_id: &Uuid) -> Result<(), CommandError> {
        self.repository
            .delete(command_id)
            .await
            .map_err(|e| e.into())
    }

    async fn get_commands(&self) -> Result<Vec<Command>, CommandError> {
        self.repository.find_all().await.map_err(|e| e.into())
    }

    async fn register_command(
        &self,
        request: String,
        chat_id: ChatId,
        message_type: MessageType,
        task: Task,
    ) -> Result<Command, CommandError> {
        let command = Command::new(
            "".to_string(),
            request,
            message_type,
            chat_id,
            "".to_string(),
            task,
        )
        .map_err(CommandError::Validation)?;

        // Persistir en el repositorio
        self.repository.save(&command).await?;

        Ok(command)
    }

    async fn update_command(&self, command: &Command) -> Result<(), CommandError> {
        self.repository.update(command).await.map_err(|e| e.into())
    }
}

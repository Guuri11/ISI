use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use super::{
    errors::CommandError,
    model::Command,
    repository::CommandRepository,
    value_objets::{ChatId, MessageType, Task},
};

#[async_trait]
pub trait CommandUseCases: Send + Sync {
    async fn delete_command(&self, command_id: &Uuid) -> Result<(), CommandError>;
    async fn get_commands(&self) -> Result<Vec<Command>, CommandError>;
    async fn register_command(
        &self,
        request: String,
        chat_id: ChatId,
        message_type: MessageType,
        task: Task,
    ) -> Result<Command, CommandError>;
    async fn update_command(&self, command: &Command) -> Result<(), CommandError>;
}

pub struct CommandUseCasesImpl {
    pub repository: Arc<dyn CommandRepository + Send + Sync>,
}

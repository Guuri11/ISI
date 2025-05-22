use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use crate::domain::{
    chat::use_cases::ChatUseCases,
    task::{model::TaskType, use_cases::TaskUseCases},
};

use super::{
    errors::CommandError, model::Command, repository::CommandRepository, value_objets::MessageType,
};

#[async_trait]
pub trait CommandUseCases: Send + Sync {
    async fn delete_command(&self, command_id: &Uuid) -> Result<(), CommandError>;
    async fn get_commands(&self) -> Result<Vec<Command>, CommandError>;
    async fn register_command(
        &self,
        request: String,
        chat_id: Option<Uuid>,
        message_type: MessageType,
        task: Option<TaskType>,
    ) -> Result<Command, CommandError>;
    async fn update_command(&self, command: &Command) -> Result<(), CommandError>;
}

pub struct CommandUseCasesImpl {
    pub repository: Arc<dyn CommandRepository + Send + Sync>,
    pub task_service: Arc<dyn TaskUseCases + Send + Sync>,
    pub chat_service: Arc<dyn ChatUseCases + Send + Sync>,
}

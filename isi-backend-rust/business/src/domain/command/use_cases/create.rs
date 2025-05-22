use std::sync::Arc;

use async_trait::async_trait;
use uuid::Uuid;

use crate::domain::{
    chat::use_cases::create::CreateChatUseCase,
    command::{
        errors::CommandError, model::Command, repository::CommandRepository,
        value_objets::MessageType,
    },
    task::{model::TaskType, use_cases::execute::ExecuteTaskUseCase},
};

#[async_trait]
pub trait CreateCommandUseCase: Send + Sync {
    async fn execute(
        &self,
        request: String,
        chat_id: Option<Uuid>,
        message_type: MessageType,
        task: Option<TaskType>,
    ) -> Result<Command, CommandError>;
}

pub struct CreateCommandUseCaseImpl {
    pub repository: Arc<dyn CommandRepository + Send + Sync>,
    pub execute_task_use_case: Arc<dyn ExecuteTaskUseCase + Send + Sync>,
    pub create_chat_use_case: Arc<dyn CreateChatUseCase + Send + Sync>,
}

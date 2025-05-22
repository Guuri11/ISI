use std::sync::Arc;

use async_trait::async_trait;
use uuid::Uuid;

use crate::domain::{
    chat::use_cases::create::CreateChatUseCase,
    command::{
        errors::CommandError,
        model::Command,
        repository::CommandRepository,
        use_cases::create::{CreateCommandUseCase, CreateCommandUseCaseImpl},
        value_objets::{ChatId, MessageType},
    },
    task::{errors::TaskError, model::TaskType, use_cases::execute::ExecuteTaskUseCase},
};

impl CreateCommandUseCaseImpl {
    pub fn new(
        repository: Arc<dyn CommandRepository + Send + Sync>,
        execute_task_use_case: Arc<dyn ExecuteTaskUseCase + Send + Sync>,
        create_chat_use_case: Arc<dyn CreateChatUseCase + Send + Sync>,
    ) -> Self {
        Self {
            repository,
            execute_task_use_case,
            create_chat_use_case,
        }
    }

    /// Handles the registration of a command when no specific task is provided.
    ///
    /// This function determines the task type based on the request, creates a command,
    /// saves it to the repository, and executes the task. The resulting command response
    /// is also saved to the repository.
    ///
    /// # Arguments
    /// * `request` - The user input or request string.
    /// * `chat_id` - The unique identifier for the chat.
    /// * `message_type` - The type of message (e.g., text, image).
    ///
    /// # Returns
    /// A `Result` containing the created `Command` or a `CommandError` if an error occurs.
    async fn handle_taskless_command(
        &self,
        request: &String,
        chat_id: ChatId,
        message_type: MessageType,
    ) -> Result<Command, CommandError> {
        let agent_response = self
            .execute_task_use_case
            .get_task_type(request)
            .await
            .map_err(|e| CommandError::TaskError(TaskError::ExecutionFailed(e.to_string())))?;

        let command = self.create_command(
            request,
            chat_id.clone(),
            message_type,
            agent_response.task.clone(),
        )?;
        self.repository.save(&command).await?;

        let command_response = self
            .execute_task_use_case
            .execute(&agent_response, chat_id)
            .await
            .map_err(|e| CommandError::TaskError(TaskError::ExecutionFailed(e.to_string())))?;

        self.repository.save(&command_response).await?;

        Ok(command_response)
    }

    /// Creates a new command instance.
    ///
    /// This function encapsulates the logic for creating a command object, ensuring
    /// that the required fields are properly initialized and validated.
    ///
    /// # Arguments
    /// * `request` - The user input or request string.
    /// * `chat_id` - The unique identifier for the chat.
    /// * `message_type` - The type of message (e.g., text, image).
    /// * `task` - The task type associated with the command.
    ///
    /// # Returns
    /// A `Result` containing the created `Command` or a `CommandError` if validation fails.
    fn create_command(
        &self,
        request: &str,
        chat_id: ChatId,
        message_type: MessageType,
        task: TaskType,
    ) -> Result<Command, CommandError> {
        Command::new(
            "".to_string(),
            request.to_string(),
            message_type,
            chat_id,
            "".to_string(),
            task,
        )
        .map_err(|e| CommandError::ValidationError(e.to_string()))
    }
}

#[async_trait]
impl CreateCommandUseCase for CreateCommandUseCaseImpl {
    async fn execute(
        &self,
        request: String,
        chat_id: Option<Uuid>,
        message_type: MessageType,
        task: Option<TaskType>,
    ) -> Result<Command, CommandError> {
        let chat = match chat_id {
            Some(chat_id) => ChatId::new(chat_id),
            None => ChatId::new(self.create_chat_use_case.execute().await.unwrap().id()),
        };

        if let Some(task) = task {
            let command = self.create_command(&request, chat, message_type, task)?;
            self.repository.save(&command).await?;
            Ok(command)
        } else {
            self.handle_taskless_command(&request, chat, message_type)
                .await
        }
    }
}

// TODO: Tests

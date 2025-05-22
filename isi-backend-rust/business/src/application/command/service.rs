use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use crate::domain::{
    chat::use_cases::ChatUseCases,
    command::{
        errors::CommandError,
        model::Command,
        repository::CommandRepository,
        use_cases::{CommandUseCases, CommandUseCasesImpl},
        value_objets::{ChatId, MessageType},
    },
    task::{errors::TaskError, model::TaskType, use_cases::TaskUseCases},
};

impl CommandUseCasesImpl {
    pub fn new(
        repository: Arc<dyn CommandRepository + Send + Sync>,
        task_service: Arc<dyn TaskUseCases + Send + Sync>,
        chat_service: Arc<dyn ChatUseCases + Send + Sync>,
    ) -> Self {
        Self {
            repository,
            task_service,
            chat_service,
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
            .task_service
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
            .task_service
            .execute_task(&agent_response, chat_id)
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
        .map_err(CommandError::Validation)
    }
}

#[async_trait]
impl CommandUseCases for CommandUseCasesImpl {
    /// Registers a new command.
    ///
    /// This function handles the registration of a command. If a specific task is provided,
    /// it creates and saves the command directly. Otherwise, it delegates to
    /// `handle_taskless_command` to determine the task type and execute the associated logic.
    ///
    /// # Arguments
    /// * `request` - The user input or request string.
    /// * `chat_id` - The unique identifier for the chat.
    /// * `message_type` - The type of message (e.g., text, image).
    /// * `task` - An optional task type to associate with the command.
    ///
    /// # Returns
    /// A `Result` containing the created `Command` or a `CommandError` if an error occurs.
    async fn register_command(
        &self,
        request: String,
        chat_id: Option<Uuid>,
        message_type: MessageType,
        task: Option<TaskType>,
    ) -> Result<Command, CommandError> {
        let chat = match chat_id {
            Some(chat_id) => ChatId::new(chat_id),
            None => ChatId::new(self.chat_service.register_chat().await.unwrap().id()),
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

    async fn delete_command(&self, command_id: &Uuid) -> Result<(), CommandError> {
        self.repository
            .delete(command_id)
            .await
            .map_err(|e| e.into())
    }

    async fn get_commands(&self) -> Result<Vec<Command>, CommandError> {
        self.repository.find_all().await.map_err(|e| e.into())
    }

    async fn update_command(&self, command: &Command) -> Result<(), CommandError> {
        self.repository.update(command).await.map_err(|e| e.into())
    }
}

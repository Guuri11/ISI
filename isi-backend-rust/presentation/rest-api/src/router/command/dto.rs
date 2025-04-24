use business::domain::command::{
    model::Command,
    value_objets::{MessageType, Task},
};
use serde::{Deserialize, Serialize};
use utoipa::ToSchema;
use uuid::Uuid;

#[derive(Debug, Clone, Deserialize, Serialize, ToSchema)]
pub enum MessageTypeDTO {
    Assistant,
    Function,
    System,
    User,
}

impl From<MessageTypeDTO> for MessageType {
    fn from(status: MessageTypeDTO) -> Self {
        match status {
            MessageTypeDTO::Assistant => MessageType::Assistant,
            MessageTypeDTO::Function => MessageType::Function,
            MessageTypeDTO::System => MessageType::System,
            MessageTypeDTO::User => MessageType::User,
        }
    }
}

impl From<MessageType> for MessageTypeDTO {
    fn from(status: MessageType) -> Self {
        match status {
            MessageType::Assistant => MessageTypeDTO::Assistant,
            MessageType::Function => MessageTypeDTO::Function,
            MessageType::System => MessageTypeDTO::System,
            MessageType::User => MessageTypeDTO::User,
        }
    }
}

#[derive(Debug, Clone, Deserialize, Serialize, ToSchema)]
pub enum TaskDTO {
    Refactor,
    Weather,
    OpenApp,
    BookmarkRecommendations,
    OtherThopics,
    LinkedinOfferRejection,
}

impl From<TaskDTO> for Task {
    fn from(status: TaskDTO) -> Self {
        match status {
            TaskDTO::Refactor => Task::Refactor,
            TaskDTO::Weather => Task::Weather,
            TaskDTO::OpenApp => Task::OpenApp,
            TaskDTO::BookmarkRecommendations => Task::BookmarkRecommendations,
            TaskDTO::OtherThopics => Task::OtherThopics,
            TaskDTO::LinkedinOfferRejection => Task::LinkedinOfferRejection,
        }
    }
}

impl From<Task> for TaskDTO {
    fn from(status: Task) -> Self {
        match status {
            Task::Refactor => TaskDTO::Refactor,
            Task::Weather => TaskDTO::Weather,
            Task::OpenApp => TaskDTO::OpenApp,
            Task::BookmarkRecommendations => TaskDTO::BookmarkRecommendations,
            Task::OtherThopics => TaskDTO::OtherThopics,
            Task::LinkedinOfferRejection => TaskDTO::LinkedinOfferRejection,
        }
    }
}

#[derive(Debug, Clone, Serialize, Deserialize, ToSchema)]
pub struct CommandInputDTO {
    pub request: String,
    pub chat_id: Uuid,
    pub message_type: MessageTypeDTO,
    pub task: TaskDTO,
}

#[derive(Debug, Clone, Serialize, Deserialize, ToSchema)]
pub struct CommandOutputDTO {
    pub id: Uuid,
    pub fav_name: String,
    pub content: String,
    pub chat_id: Uuid,
    pub message_type: MessageTypeDTO,
    pub task: TaskDTO,
    pub created_at: String,
    pub updated_at: String,
}

impl CommandOutputDTO {
    pub fn from_command(command: &Command) -> Self {
        CommandOutputDTO {
            id: command.id(),
            fav_name: command.fav_name().to_string(),
            content: command.content().to_string(),
            chat_id: command.chat().id(),
            message_type: MessageTypeDTO::from(command.message_type().clone()),
            task: TaskDTO::from(command.task().clone()),
            created_at: command.created_at().to_string(),
            updated_at: command.updated_at().to_string(),
        }
    }
}

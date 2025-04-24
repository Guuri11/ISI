use business::domain::command::{
    model::Command,
    value_objets::{ChatId, MessageType as MessageTypeDomain, Task},
};
use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};
use sqlx::prelude::{FromRow, Type};
use uuid::Uuid;

#[derive(Debug, Serialize, Deserialize, Type, Clone, PartialEq)]
#[sqlx(type_name = "message_type")]
pub enum MessageType {
    Assistant,
    User,
    System,
    Function,
}

#[derive(Debug, Serialize, Deserialize, FromRow)]
pub struct CommandDb {
    pub id: Uuid,
    pub log: Option<String>,
    pub content: String,
    pub message_type: MessageType,
    pub chat_id: Uuid,
    pub fav_name: Option<String>,
    pub created_at: NaiveDateTime,
    pub updated_at: NaiveDateTime,
}

impl From<&Command> for CommandDb {
    fn from(command: &Command) -> Self {
        CommandDb {
            id: command.id(),
            log: Some(command.log().clone()),
            content: command.content().clone(),
            message_type: match command.message_type() {
                MessageTypeDomain::Assistant => MessageType::Assistant,
                MessageTypeDomain::User => MessageType::User,
                MessageTypeDomain::System => MessageType::System,
                MessageTypeDomain::Function => MessageType::Function,
            },
            chat_id: command.chat().id(),
            fav_name: Some(command.fav_name().clone()),
            created_at: command.created_at(),
            updated_at: command.updated_at(),
        }
    }
}

impl TryFrom<&CommandDb> for Command {
    type Error = String;

    fn try_from(db: &CommandDb) -> Result<Self, Self::Error> {
        Ok(Command::from_repository(
            db.id,
            db.log.clone().map_or_else(|| "".to_string(), |log| log),
            db.content.clone(),
            match db.message_type {
                MessageType::Assistant => MessageTypeDomain::Assistant,
                MessageType::User => MessageTypeDomain::User,
                MessageType::System => MessageTypeDomain::System,
                MessageType::Function => MessageTypeDomain::Function,
            },
            ChatId::new(db.chat_id),
            db.fav_name
                .clone()
                .map_or_else(|| "".to_string(), |name| name),
            Task::OtherThopics,
            db.created_at,
            db.updated_at,
        ))
    }
}

impl TryFrom<CommandDb> for Command {
    type Error = String;

    fn try_from(db: CommandDb) -> Result<Self, Self::Error> {
        Command::try_from(&db)
    }
}

use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};
use uuid::Uuid;

use crate::domain::task::model::TaskType;

use super::value_objets::{ChatId, MessageType};

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct Command {
    id: Uuid,
    log: String,
    content: String,
    message_type: MessageType,
    chat: ChatId,
    fav_name: String,
    task: TaskType,
    created_at: NaiveDateTime,
    updated_at: NaiveDateTime,
}

impl Command {
    pub fn new(
        log: String,
        content: String,
        message_type: MessageType,
        chat: ChatId,
        fav_name: String,
        task: TaskType,
    ) -> Result<Self, String> {
        Ok(Self {
            id: Uuid::new_v4(),
            log,
            content,
            message_type,
            chat,
            fav_name,
            task,
            created_at: chrono::Utc::now().naive_utc(),
            updated_at: chrono::Utc::now().naive_utc(),
        })
    }

    pub fn from_repository(
        id: Uuid,
        log: String,
        content: String,
        message_type: MessageType,
        chat: ChatId,
        fav_name: String,
        task: TaskType,
        created_at: NaiveDateTime,
        updated_at: NaiveDateTime,
    ) -> Self {
        Self {
            id,
            log,
            content,
            message_type,
            chat,
            fav_name,
            task,
            created_at,
            updated_at,
        }
    }

    pub fn id(&self) -> Uuid {
        self.id
    }

    pub fn log(&self) -> &String {
        &self.log
    }

    pub fn content(&self) -> &String {
        &self.content
    }

    pub fn message_type(&self) -> &MessageType {
        &self.message_type
    }

    pub fn chat(&self) -> &ChatId {
        &self.chat
    }

    pub fn fav_name(&self) -> &String {
        &self.fav_name
    }

    pub fn task(&self) -> &TaskType {
        &self.task
    }

    pub fn created_at(&self) -> NaiveDateTime {
        self.created_at
    }

    pub fn updated_at(&self) -> NaiveDateTime {
        self.updated_at
    }
}

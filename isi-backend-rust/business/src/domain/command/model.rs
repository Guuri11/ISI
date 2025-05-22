// === Bloque de derive (elige según tu caso) ===
// #[derive(Debug)]                       // Para imprimir en consola (útil en desarrollo)
// #[derive(Clone)]                      // Para clonar structs fácilmente
// #[derive(Serialize, Deserialize)]     // Para convertir desde/hacia JSON u otros formatos
// #[derive(PartialEq, Eq, Hash)]        // Para comparar, o usar en HashMaps o HashSets
// #[derive(Default)]                    // Para construir una instancia vacía si aplica

use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};
use uuid::Uuid;

use crate::domain::{identifiable_model::Identifiable, task::model::TaskType};

use super::{
    errors::CommandError,
    value_objets::{ChatId, MessageType},
};

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct Command {
    id: Uuid,
    created_at: NaiveDateTime,
    updated_at: NaiveDateTime,
    deleted: bool,
    deleted_at: Option<NaiveDateTime>,
    log: String,
    content: String,
    message_type: MessageType,
    chat: ChatId,
    fav_name: String,
    task: TaskType,
}

impl Command {
    pub fn new(
        log: String,
        content: String,
        message_type: MessageType,
        chat: ChatId,
        fav_name: String,
        task: TaskType,
    ) -> Result<Self, CommandError> {
        Ok(Self {
            id: Uuid::new_v4(),
            created_at: chrono::Utc::now().naive_utc(),
            updated_at: chrono::Utc::now().naive_utc(),
            deleted: false,
            deleted_at: None,
            log,
            content,
            message_type,
            chat,
            fav_name,
            task,
        })
    }

    pub fn from_repository(
        id: Uuid,
        created_at: NaiveDateTime,
        updated_at: NaiveDateTime,
        deleted: bool,
        deleted_at: Option<NaiveDateTime>,
        log: String,
        content: String,
        message_type: MessageType,
        chat: ChatId,
        fav_name: String,
        task: TaskType,
    ) -> Self {
        Self {
            id,
            created_at,
            updated_at,
            deleted,
            deleted_at,
            log,
            content,
            message_type,
            chat,
            fav_name,
            task,
        }
    }

    pub fn id(&self) -> Uuid {
        self.id
    }

    pub fn created_at(&self) -> NaiveDateTime {
        self.created_at
    }

    pub fn updated_at(&self) -> NaiveDateTime {
        self.updated_at
    }

    pub fn deleted(&self) -> bool {
        self.deleted
    }

    pub fn deleted_at(&self) -> Option<NaiveDateTime> {
        self.deleted_at
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
}

impl Identifiable for Command {
    fn id(&self) -> Uuid {
        self.id
    }
    fn created_at(&self) -> NaiveDateTime {
        self.created_at
    }
    fn updated_at(&self) -> NaiveDateTime {
        self.updated_at
    }
    fn deleted(&self) -> bool {
        self.deleted
    }
    fn deleted_at(&self) -> Option<NaiveDateTime> {
        self.deleted_at
    }
}

// TODO: TESTS
#[cfg(test)]
mod tests {
    #[test]
    fn should_use_case_when_condition() {
        // Arrange
        // Act
        // Assert
    }
}

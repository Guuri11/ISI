use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Debug, Clone, Deserialize, Serialize)]
pub enum MessageType {
    Assistant,
    Function,
    System,
    User,
}

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct ChatId {
    id: Uuid,
}

impl ChatId {
    pub fn new(id: Uuid) -> Self {
        Self { id }
    }

    pub fn id(&self) -> Uuid {
        self.id
    }
}

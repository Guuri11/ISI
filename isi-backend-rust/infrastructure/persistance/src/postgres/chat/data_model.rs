use business::domain::chat::model::Chat;
use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};
use sqlx::prelude::FromRow;
use uuid::Uuid;

#[derive(Debug, Serialize, Deserialize, FromRow)]
pub struct ChatDb {
    pub id: Uuid,
    pub created_at: NaiveDateTime,
    pub updated_at: NaiveDateTime,
}

impl From<&Chat> for ChatDb {
    fn from(chat: &Chat) -> Self {
        ChatDb {
            id: chat.id(),
            created_at: chat.created_at(),
            updated_at: chat.updated_at(),
        }
    }
}

impl TryFrom<&ChatDb> for Chat {
    type Error = String;

    fn try_from(db: &ChatDb) -> Result<Self, Self::Error> {
        Ok(Chat::from_repository(db.id, db.created_at, db.updated_at))
    }
}

impl TryFrom<ChatDb> for Chat {
    type Error = String;

    fn try_from(db: ChatDb) -> Result<Self, Self::Error> {
        Chat::try_from(&db)
    }
}

use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct Chat {
    id: Uuid,
    created_at: NaiveDateTime,
    updated_at: NaiveDateTime,
}

impl Chat {
    pub fn new() -> Result<Self, String> {
        Ok(Self {
            id: Uuid::new_v4(),
            created_at: chrono::Utc::now().naive_utc(),
            updated_at: chrono::Utc::now().naive_utc(),
        })
    }

    pub fn from_repository(id: Uuid, created_at: NaiveDateTime, updated_at: NaiveDateTime) -> Self {
        Self {
            id,
            created_at,
            updated_at,
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
}

// === Bloque de derive (elige según tu caso) ===
// #[derive(Debug)]                       // Para imprimir en consola (útil en desarrollo)
// #[derive(Clone)]                      // Para clonar structs fácilmente
// #[derive(Serialize, Deserialize)]     // Para convertir desde/hacia JSON u otros formatos
// #[derive(PartialEq, Eq, Hash)]        // Para comparar, o usar en HashMaps o HashSets
// #[derive(Default)]                    // Para construir una instancia vacía si aplica

use chrono::NaiveDateTime;
use serde::{Deserialize, Serialize};
use uuid::Uuid;

use crate::domain::identifiable_model::Identifiable;

use super::errors::ChatError;

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct Chat {
    id: Uuid,
    created_at: NaiveDateTime,
    updated_at: NaiveDateTime,
    deleted: bool,
    deleted_at: Option<NaiveDateTime>,
}

impl Chat {
    pub fn new() -> Result<Self, ChatError> {
        Ok(Self {
            id: Uuid::new_v4(),
            created_at: chrono::Utc::now().naive_utc(),
            updated_at: chrono::Utc::now().naive_utc(),
            deleted: false,
            deleted_at: None,
        })
    }

    pub fn from_repository(
        id: Uuid,
        created_at: NaiveDateTime,
        updated_at: NaiveDateTime,
        deleted: bool,
        deleted_at: Option<NaiveDateTime>,
    ) -> Self {
        Self {
            id,
            created_at,
            updated_at,
            deleted,
            deleted_at,
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
}

impl Identifiable for Chat {
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

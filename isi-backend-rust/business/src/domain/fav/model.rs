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

use super::errors::FavError;

#[derive(Debug, Clone, Deserialize, Serialize)]
pub struct Fav {
    id: Uuid,
    created_at: NaiveDateTime,
    updated_at: NaiveDateTime,
    deleted: bool,
    deleted_at: Option<NaiveDateTime>,
    name: String,
}

// TODO: save image
impl Fav {
    pub fn new(name: String) -> Result<Self, FavError> {
        // if ... return Err(FavError::ValidationError("custom_message_here".to_string()))
        if name.is_empty() {
            return Err(FavError::ValidationError("name_empty".to_string()));
        }

        Ok(Self {
            id: Uuid::new_v4(),
            created_at: chrono::Utc::now().naive_utc(),
            updated_at: chrono::Utc::now().naive_utc(),
            deleted: false,
            deleted_at: None,
            name,
        })
    }

    pub fn from_repository(
        id: Uuid,
        created_at: NaiveDateTime,
        updated_at: NaiveDateTime,
        deleted: bool,
        deleted_at: Option<NaiveDateTime>,
        name: String,
    ) -> Self {
        Self {
            id,
            created_at,
            updated_at,
            deleted,
            deleted_at,
            name,
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

    pub fn name(&self) -> &str {
        &self.name
    }
}

impl Identifiable for Fav {
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

// TODO: Tests
#[cfg(test)]
mod tests {

    #[test]
    fn should_use_case_when_condition() {
        // Arrange
        // Act
        // Assert
    }
}

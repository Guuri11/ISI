use chrono::NaiveDateTime;
use uuid::Uuid;

/// Trait para entidades con identificador y timestamps
pub trait Identifiable {
    fn id(&self) -> Uuid;
    fn created_at(&self) -> NaiveDateTime;
    fn updated_at(&self) -> NaiveDateTime;
    fn deleted(&self) -> bool;
    fn deleted_at(&self) -> Option<NaiveDateTime>;
}

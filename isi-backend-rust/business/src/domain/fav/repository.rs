use async_trait::async_trait;
use uuid::Uuid;

use crate::domain::errors::RepositoryError;

use super::model::Fav;

#[async_trait]
pub trait FavRepository: Send + Sync {
    async fn save(&self, fav: &Fav) -> Result<(), RepositoryError>;
    async fn find_all(&self) -> Result<Vec<Fav>, RepositoryError>;
    async fn find_by_id(&self, id: &Uuid) -> Result<Fav, RepositoryError>;
    async fn find_by_name(&self, name: &str) -> Result<Fav, RepositoryError>;
    async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError>;
    async fn update(&self, fav: &Fav) -> Result<(), RepositoryError>;
}

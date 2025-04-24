use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use super::{errors::FavError, model::Fav, repository::FavRepository};

#[async_trait]
pub trait FavUseCases: Send + Sync {
    async fn delete_fav(&self, fav_id: &Uuid) -> Result<(), FavError>;
    async fn get_favs(&self) -> Result<Vec<Fav>, FavError>;
    async fn get_fav_by_id(&self, fav_id: &Uuid) -> Result<Fav, FavError>;
    async fn get_fav_by_name(&self, fav_name: &str) -> Result<Fav, FavError>;
    async fn register_fav(&self, license_plate_number: String) -> Result<Fav, FavError>;
    async fn update_fav(&self, fav: &Fav) -> Result<(), FavError>;
}

pub struct FavUseCasesImpl {
    pub repository: Arc<dyn FavRepository + Send + Sync>,
}

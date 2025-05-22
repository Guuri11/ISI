use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use crate::domain::fav::{errors::FavError, repository::FavRepository};

#[async_trait]
pub trait DeleteFavUseCase: Send + Sync {
    async fn execute(&self, id: &Uuid) -> Result<(), FavError>;
}

pub struct DeleteFavUseCaseImpl {
    pub repository: Arc<dyn FavRepository + Send + Sync>,
}

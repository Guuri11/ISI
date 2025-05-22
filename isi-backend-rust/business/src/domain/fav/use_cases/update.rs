use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::fav::{errors::FavError, model::Fav, repository::FavRepository};

#[async_trait]
pub trait UpdateFavUseCase: Send + Sync {
    async fn execute(&self, entity: &Fav) -> Result<Fav, FavError>;
}

pub struct UpdateFavUseCaseImpl {
    pub repository: Arc<dyn FavRepository + Send + Sync>,
}

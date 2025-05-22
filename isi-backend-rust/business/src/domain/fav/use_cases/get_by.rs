use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::fav::{errors::FavError, model::Fav, repository::FavRepository};

#[async_trait]
pub trait GetByFavUseCase: Send + Sync {
    async fn execute(&self) -> Result<Vec<Fav>, FavError>;
}

pub struct GetByFavUseCaseImpl {
    pub repository: Arc<dyn FavRepository + Send + Sync>,
}

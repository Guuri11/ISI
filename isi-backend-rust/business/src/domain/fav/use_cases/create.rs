use std::sync::Arc;

use async_trait::async_trait;

use crate::domain::{
    fav::{errors::FavError, model::Fav, repository::FavRepository},
    screenshot::service::ScreenshotService,
};

#[async_trait]
pub trait CreateFavUseCase: Send + Sync {
    async fn execute(&self, name: String) -> Result<Fav, FavError>;
}

pub struct CreateFavUseCaseImpl {
    pub repository: Arc<dyn FavRepository + Send + Sync>,
    pub screenshot_service: Arc<dyn ScreenshotService + Send + Sync>,
}

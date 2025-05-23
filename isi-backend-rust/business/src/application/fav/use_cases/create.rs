use std::sync::Arc;

use async_trait::async_trait;
use tracing::info;

use crate::domain::{
    fav::{
        errors::FavError,
        model::Fav,
        repository::FavRepository,
        use_cases::create::{CreateFavUseCase, CreateFavUseCaseImpl},
    },
    screenshot::service::ScreenshotService,
};

impl CreateFavUseCaseImpl {
    pub fn new(
        repository: Arc<dyn FavRepository + Send + Sync>,
        screenshot_service: Arc<dyn ScreenshotService + Send + Sync>,
    ) -> Self {
        Self {
            repository,
            screenshot_service,
        }
    }
}

#[async_trait]
impl CreateFavUseCase for CreateFavUseCaseImpl {
    async fn execute(&self, name: String) -> Result<Fav, FavError> {
        info!("Creación de fav");
        let file_name = format!("{}_{}.jpg", name.replace(" ", "-"), uuid::Uuid::new_v4());

        let entity = Fav::new(file_name.clone())?;

        self.screenshot_service
            .shoot(file_name)
            .await
            .map_err(FavError::ScreenshotError)?;

        self.repository.save(&entity).await?;

        Ok(entity)
    }
}

// TODO: Implementar tests

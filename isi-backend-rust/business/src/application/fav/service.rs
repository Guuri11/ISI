use std::sync::Arc;

use async_trait::async_trait;

use uuid::Uuid;

use crate::domain::fav::{
    errors::FavError,
    model::Fav,
    repository::FavRepository,
    use_cases::{FavUseCases, FavUseCasesImpl},
};

impl FavUseCasesImpl {
    pub fn new(repository: Arc<dyn FavRepository + Send + Sync>) -> Self {
        Self { repository }
    }
}

#[async_trait]
impl FavUseCases for FavUseCasesImpl {
    async fn delete_fav(&self, fav_id: &Uuid) -> Result<(), FavError> {
        self.repository.delete(fav_id).await.map_err(|e| e.into())
    }

    async fn get_favs(&self) -> Result<Vec<Fav>, FavError> {
        self.repository.find_all().await.map_err(|e| e.into())
    }

    async fn get_fav_by_id(&self, fav_id: &Uuid) -> Result<Fav, FavError> {
        self.repository
            .find_by_id(fav_id)
            .await
            .map_err(|e| e.into())
    }

    async fn get_fav_by_name(&self, fav_name: &str) -> Result<Fav, FavError> {
        self.repository
            .find_by_name(fav_name)
            .await
            .map_err(|e| e.into())
    }

    async fn register_fav(&self, fav_name: String) -> Result<Fav, FavError> {
        let fav = Fav::new(fav_name).map_err(FavError::Validation)?;

        self.repository.save(&fav).await?;

        Ok(fav)
    }

    async fn update_fav(&self, fav: &Fav) -> Result<(), FavError> {
        self.repository.update(fav).await.map_err(|e| e.into())
    }
}

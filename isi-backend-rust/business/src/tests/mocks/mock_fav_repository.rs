use async_trait::async_trait;
use mockall::mock;
use uuid::Uuid;

use crate::domain::errors::RepositoryError;
use crate::domain::fav::model::Fav;
use crate::domain::fav::repository::FavRepository;

mock! {
    pub FavRepository {}

    #[async_trait]
    impl FavRepository for FavRepository {
        async fn save(&self, fav: &Fav) -> Result<(), RepositoryError>;
        async fn find_all(&self) -> Result<Vec<Fav>, RepositoryError>;
        async fn find_by_id(&self, id: &Uuid) -> Result<Fav, RepositoryError>;
        async fn find_by_name(&self, name: &str) -> Result<Fav, RepositoryError>;
        async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError>;
        async fn update(&self, fav: &Fav) -> Result<(), RepositoryError>;
    }
}

impl MockFavRepository {
    pub fn with_save_returning_ok() -> Self {
        let mut mock = Self::new();
        mock.expect_save().returning(|_| Ok(()));
        mock
    }
}

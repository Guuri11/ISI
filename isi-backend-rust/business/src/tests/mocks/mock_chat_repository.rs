use async_trait::async_trait;
use mockall::mock;
use uuid::Uuid;

use crate::domain::chat::model::Chat;
use crate::domain::chat::repository::ChatRepository;
use crate::domain::errors::RepositoryError;

mock! {
    pub ChatRepository {}

    #[async_trait]
    impl ChatRepository for ChatRepository {
        async fn save(&self, chat: &Chat) -> Result<(), RepositoryError>;
        async fn find_all(&self) -> Result<Vec<Chat>, RepositoryError>;
        async fn find_by_id(&self, id: &Uuid) -> Result<Chat, RepositoryError>;
        async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError>;
        async fn update(&self, chat: &Chat) -> Result<(), RepositoryError>;
    }
}

impl MockChatRepository {
    pub fn with_save_returning_ok() -> Self {
        let mut mock = Self::new();
        mock.expect_save().returning(|_| Ok(()));
        mock
    }
}

use async_trait::async_trait;
use mockall::mock;
use uuid::Uuid;

use crate::domain::command::model::Command;
use crate::domain::command::repository::CommandRepository;
use crate::domain::errors::RepositoryError;

mock! {
    pub CommandRepository {}

    #[async_trait]
    impl CommandRepository for CommandRepository {
        async fn save(&self, command: &Command) -> Result<(), RepositoryError>;
        async fn find_all(&self) -> Result<Vec<Command>, RepositoryError>;
        async fn find_by_id(&self, id: &Uuid) -> Result<Command, RepositoryError>;
        async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError>;
        async fn update(&self, command: &Command) -> Result<(), RepositoryError>;
    }
}

impl MockCommandRepository {
    pub fn with_save_returning_ok() -> Self {
        let mut mock = Self::new();
        mock.expect_save().returning(|_| Ok(()));
        mock
    }
}

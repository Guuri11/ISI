use async_trait::async_trait;
use business::domain::{
    chat::{model::Chat, repository::ChatRepository as CoreChatRepository},
    errors::RepositoryError,
};
use sqlx::{Pool, Postgres};
use uuid::Uuid;

use super::data_model::ChatDb;

pub struct ChatRepository {
    pool: Pool<Postgres>,
}

impl ChatRepository {
    pub fn new(pool: Pool<Postgres>) -> Self {
        ChatRepository { pool }
    }
}

#[async_trait]
impl CoreChatRepository for ChatRepository {
    async fn save(&self, chat: &Chat) -> Result<(), RepositoryError> {
        let chat_db = ChatDb::from(chat);

        sqlx::query!(
            r#"
            INSERT INTO chats (id, created_at, updated_at)
            VALUES ($1, $2, $3)
            "#,
            chat_db.id,
            chat_db.created_at,
            chat_db.updated_at,
        )
        .execute(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?;

        Ok(())
    }

    async fn find_all(&self) -> Result<Vec<Chat>, RepositoryError> {
        let results = sqlx::query_as!(
            ChatDb,
            r#"
            SELECT id,created_at, updated_at
            FROM chats
            "#,
        )
        .fetch_all(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?;

        results
            .into_iter()
            .map(|chat_db| Chat::try_from(&chat_db).map_err(RepositoryError::Persistence))
            .collect()
    }

    async fn find_by_id(&self, id: &Uuid) -> Result<Chat, RepositoryError> {
        sqlx::query_as!(
            ChatDb,
            r#"
            SELECT id, created_at, updated_at
            FROM chats
            WHERE id = $1
            "#,
            id
        )
        .fetch_optional(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?
        .ok_or_else(|| RepositoryError::NotFound(format!("Chat with id {} not found", id)))
        .and_then(|chat_db| Chat::try_from(&chat_db).map_err(RepositoryError::Persistence))
    }

    async fn delete(&self, id: &Uuid) -> Result<(), RepositoryError> {
        let affected = sqlx::query!(
            r#"
            DELETE FROM chats
            WHERE id = $1
            "#,
            id
        )
        .execute(&self.pool)
        .await
        .map_err(|e| RepositoryError::DatabaseError(e.to_string()))?
        .rows_affected();

        if affected == 0 {
            return Err(RepositoryError::NotFound(format!(
                "Chat with id {} not found",
                id
            )));
        }

        Ok(())
    }

    async fn update(&self, chat: &Chat) -> Result<(), RepositoryError> {
        let chat_db = ChatDb::from(chat);

        let result = sqlx::query!(
            r#"
            UPDATE chats
            SET created_at = $2, updated_at = $3
            WHERE id = $1
            "#,
            chat_db.id,
            chat_db.created_at,
            chat_db.updated_at,
        )
        .execute(&self.pool)
        .await;

        match result {
            Ok(affected) => {
                if affected.rows_affected() == 0 {
                    return Err(RepositoryError::NotFound(format!(
                        "Chat with id {} not found",
                        chat.id()
                    )));
                }
                Ok(())
            }
            Err(e) => Err(RepositoryError::DatabaseError(e.to_string())),
        }
    }
}
